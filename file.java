package com.tmcc.ebaoc.core.workflow;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.Session;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.lucene.queryparser.flexible.core.util.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowData;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.day.cq.dam.api.Asset;
import com.day.cq.dam.api.Rendition;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.SearchResult;
import com.tmcc.ebaoc.core.docxconverter.DocReader;

@Component(label = "HTML Conversion", description = ".docx to .html Conversion")
@Properties({
		@Property(name = "service.description", value = "HTML conversion Process implementation.", propertyPrivate = true),
		@Property(label = "Workflow Label", name = "process.label", value = "Document to HTML Conversion", description = "Storing Converted HTML file") })
@Service
public class DocxToHTMLConversion implements WorkflowProcess {

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	@Reference
	ResourceResolverFactory resolverFactory;
	@Reference
	private QueryBuilder builder;
	String currentAsset;

	@Override
	public void execute(WorkItem workItem, WorkflowSession wfs, MetaDataMap arg2) throws WorkflowException {
		final WorkflowData data = workItem.getWorkflowData();
		ResourceResolver resolver = wfs.adaptTo(ResourceResolver.class);
		currentAsset = data.getPayload().toString();
		String docxFilePath; // these are to mention the path of the source file
		String outputFileName = ""; // this mentions the name of the output file
		String htmlMarkup = ""; // this contains the html markup text as
								// converted from the docx file
		DocReader dr = new DocReader();
		try {
			Resource resource = resolver.getResource(currentAsset);
			docxFilePath = currentAsset;
			outputFileName = currentAsset.substring(currentAsset.lastIndexOf('/') + 1, currentAsset.lastIndexOf('.'));
			Asset asset = resource.adaptTo(Asset.class); // you probably need a
															// null check
															// here...
			//System.out.println("asset");
			Rendition original = asset.getOriginal();
			InputStream stream = null;
			if (original != null)

			{ // it is rare, but some assets might not have an original
				// rendition

				stream = original.getStream(); // do something with the stream

			}
			htmlMarkup = dr.convertDocxFile(stream);
			outputTxt(htmlMarkup, outputFileName, resolver);

			stream.close();
		} catch (Exception e) {
			log.error("ERROR: Generic Exception while reading- " + e.getMessage());
		}

	}

	public void outputTxt(String htmlMarkup, String outputFileName, ResourceResolver resolver){ 
		Session session = resolver.adaptTo(Session.class);
		String PATH_VAL_TFS;
		String PATH_VAL_LFS;
		try {
			if (session != null) {
				String[] arr = currentAsset.split("/");

				if (arr[4].indexOf("en_us") != -1) {
					if (arr[5].indexOf("mweb") != -1) {
						// Query Path for en_us MWeb
						PATH_VAL_TFS = "/content/tmcc_eba_oc/en/us/toyotafinancial/mweb";
						PATH_VAL_LFS = "/content/tmcc_eba_oc/en/us/lexusfinancial/mweb";
					} else if (arr[5].indexOf("web") != -1) {
						// Query Path for en_us Web
						PATH_VAL_TFS = "/content/tmcc_eba_oc/en/us/toyotafinancial/web";
						PATH_VAL_LFS = "/content/tmcc_eba_oc/en/us/lexusfinancial/web";

					} else {
						// Query Path for en_us MApp
						PATH_VAL_TFS = "/content/tmcc_eba_oc/en/us/toyotafinancial/mapp";
						PATH_VAL_LFS = "/content/tmcc_eba_oc/en/us/lexusfinancial/mapp";
					}

				} else {
					// Query Path for es_pr MApp
					PATH_VAL_TFS = "/content/tmcc_eba_oc/es/pr/toyotafinancial/mapp";
					PATH_VAL_LFS = "/content/tmcc_eba_oc/es/pr/lexusfinancial/mapp";
				}
				Map<String, String> queryMap = new HashMap<String, String>();
				queryMap.put("group.1_path", PATH_VAL_TFS);
				queryMap.put("group.2_path", PATH_VAL_LFS);
				queryMap.put("property", "sling:resourceType");
				queryMap.put("property.value", "tmcc-ebaoc/components/content/htmlComponent");
				queryMap.put("group.1_group.p.or", "true");
				queryMap.put("group.2_group.p.or", "true");
				queryMap.put("group.p.or", "true");
				queryMap.put("p.limit", "-1");
				Query query = builder.createQuery(PredicateGroup.create(queryMap), session);
				SearchResult results = query.getResult();
				// Get hit nodes from search results
				Iterator<Node> nodeItr = results.getNodes();
				Node hitNode = null;
				while (nodeItr.hasNext()) {
					hitNode = (Node) nodeItr.next();
					String getPagePath[] = hitNode.getPath().split("tmcc_eba_oc/");
					log.error("getPagePath[1] --> " + getPagePath[1]);
					if (getPagePath[1].contains(outputFileName.toLowerCase())) {
						hitNode.setProperty("htmlContent", htmlMarkup);
						session.save();
					}
				}

				// Save the session changes and log out
				session.save();
				// session.logout();

			}

		} catch (Exception e) {
			log.error("outputTxt while setting" + e);
		}
	}
}

package br.gov.ufpe.sparql;

import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import org.codehaus.jackson.JsonGenerationException;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Model;

import de.fuberlin.wiwiss.d2rq.ModelD2RQ;

@Path("/")
public class RodaSparqlSIM {

	/**
	 * @param rs
	 * @param i
	 * @throws IOException
	 */ 
	@GET
	public String mediatorMethod(@QueryParam("query") String queryString)
			throws IOException {
		Model mSINAN = new ModelD2RQ("file:sim.n3");
  
		// WS2  
		Query q2 = QueryFactory.create(queryString);
		ResultSet rs2 = QueryExecutionFactory.create(q2, mSINAN).execSelect(); 
		String xml = ResultSetFormatter.asXMLString(rs2);     

		return xml;        
   
	}

	public void serialize(Model modelSINAN) throws JsonGenerationException, IOException {  
		
//		RDFWriterFImpl write = new RDFWriterFImpl();  
//		write.getWriter().write(modelSINAN, os, null);   
//	
//		InputStream in = new ByteArrayInputStream(os.toByteArray());
//		
//		JenaWriterRdfJson writer = new JenaWriterRdfJson();
//		ByteArrayOutputStream os = new ByteArrayOutputStream();
// 
//		writer.write(modelSINAN, os, "RDF/JSON");
//
//		StringWriter swriter = new StringWriter();  
//		ObjectMapper mapper = new ObjectMapper();
//
//		JsonGenerator jsonGenerator = mapper.getJsonFactory()
//				.createJsonGenerator(swriter);
//		jsonGenerator.writeRawValue(os.toString().replaceAll("\\n", "")
//				.replaceAll("\\s+", " ").replaceAll("", ""));
//
//		String jsonbytes = os.toString().trim().replaceAll("\\n", "")
//				.replaceAll("\\s+", " ").replaceAll("", "");
//		System.out.println(jsonbytes); 
//
//		InputStream in = new ByteArrayInputStream(jsonbytes.getBytes());
//		// Parse the JSON back to make sure we always write valid JSO
//		JSON.parse(in);
//		JSONInput json = new JSONInput(in); 
	}

}

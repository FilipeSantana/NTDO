package br.gov.ufpe;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;

public class SinanSimD2RQClient {

	// final static String serviceEndpoint = "http://sparql.org/sparql";

	final static String serviceEndpoint = "http://dbpedia.org/sparql";

	public static void main(String args[]) {

		String queryString2 = "PREFIX ntdo: <http://purl.org/ntdo/ntdo.owl#> " 
	    + "PREFIX ntdo: <http://www.cin.ufpe.br/~ntdo/OWLFiles/ntdo.owl#> "    
	    + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " 
	    + "SELECT * "  
	    + "   from <http://www.cin.ufpe.br/~ntdo/OWLFiles/ntdo.owl> " 
		+ "   { " 
		+ "       SERVICE <http://localhost:8080/SimD2RQServer/sparql/> "
		+ "       { "
	    + "	    ?death rdf:type ntdo:DeathProcess;								" 
	    + "			            ntdo:hasDeathPatient ?patient;" 
	    + "			            ntdo:hasDeathPrimaryCause ?causabas.} " 
	    
	    + "      OPTIONAL {  SERVICE <http://localhost:8080/SinanD2RQServer/sparql/> {" 
	    + "	        ?notification rdf:type ntdo:HealthSurveillanceNotificationAction;			" 
	    + "		                ntdo:existsAt ?dataNasc;    " 
	    + "                     ntdo:realizationOf ?agravo;"  
	    + "					    ntdo:hasLocus ?localOcorr;" 
	    + "					    ntdo:hasParticipant ?patient.   " 
	    + "		  ?patient      ntdo:hasLocus ?localResid."  
	    + "	   } } }";  

		Query query = QueryFactory.create(queryString2);
		// QueryExecution qe =
		// QueryExecutionFactory.sparqlService(serviceEndpoint, query);
		QueryExecution qe = QueryExecutionFactory.create(query);

		try {
			ResultSet rs = qe.execSelect();

			if (rs.hasNext()) {
				// show the result, more can be done here
				System.out.println(ResultSetFormatter.asText(rs));
			}
		} finally {
			qe.close();
		}
		System.out.println("\nall done.");
	}
}

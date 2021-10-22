package br.gov.ufpe;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

public class RodarSparql {

	private static OntModel model;

	public static void loadOntology(String url) {

		if (model == null) {
			model = ModelFactory.createOntologyModel();

			try {
				model.read(new FileInputStream(url), "");
			} catch (FileNotFoundException e) {
				System.out.println(e.getMessage());
			}
		}
	}

	public static void main(String[] args) {
		RodarSparql.loadOntology("C:\\Users\\Roberta\\Desktop\\ntdo1.owl");
		RodarSparql.rodarSparql();
	}

	/**
	 * Ano Novo"^^xsd:string
	 * 
	 * @param sentenca
	 * @return
	 */
	public static ArrayList<String> rodarSparql() {
		ArrayList<String> retorno = new ArrayList<String>();

		// here's the query
		String queryString = " PREFIX ntdo:<http://www.cin.ufpe.br/~ntdo/ntdo.owl#> " +
			" PREFIX biotop:<http://purl.org/biotop/biotop.owl#> " + 
			" PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> " +
			" SELECT  COUNT(*) AS ?evento " + 
			" WHERE { ?evento rdf:type ntdo:DeathEvent; " +
			" biotop:hasLocus ?locus;  " +
			" biotop:hasProcessualPart ?processualPart;  " +
			" biotop:hasPatient ?pessoa.  " +
			" ?pessoa ntdo:sex ?sex.  " +
			" ?pessoa ntdo:racecolor ?racecolor.  " +
			" ?pessoa ntdo:age ?age.  " +
			" ?pessoa biotop:hasLocus ?localresidencia. " +
			" ?localresidencia ntdo:name ?nameresid.  " +
			" ?locus ntdo:name ?locusocor.  " +
			" ?processualPart ntdo:pathologicalProcess ?path.  " +
			" FILTER( ?racecolor= 'Branca'^^xsd:string).  " +
			" }} ";

		// create the query object
		QueryExecution query = QueryExecutionFactory.create(queryString, model);
 
		com.hp.hpl.jena.query.ResultSet results = query.execSelect();

		for (; results.hasNext();) {
			QuerySolution sol = results.nextSolution();

			System.out.print(sol.get("evento") + " - ");
			
			Resource res = sol.get("pessoa").asResource();

			Property propriedadeSexo = model
					.getProperty("http://www.cin.ufpe.br/~ntdo/ntdo.owl#sex");

			System.out.println(res.getProperty(propriedadeSexo).getString()); 
		}

		return retorno;

	}

}

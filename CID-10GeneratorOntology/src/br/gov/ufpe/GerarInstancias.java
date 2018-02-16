package br.gov.ufpe;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.hp.hpl.jena.enhanced.EnhGraph;
import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.ontology.AllValuesFromRestriction;
import com.hp.hpl.jena.ontology.ComplementClass;
import com.hp.hpl.jena.ontology.HasValueRestriction;
import com.hp.hpl.jena.ontology.IntersectionClass;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.SomeValuesFromRestriction;
import com.hp.hpl.jena.ontology.impl.ComplementClassImpl;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFList;
import com.hp.hpl.jena.rdf.model.RDFNode;

public class GerarInstancias {

	private static OntModel model;

	public static void main(String[] args) throws SQLException,
			ClassNotFoundException, IOException {
		Connection conn = GerarInstancias.getConnection();

		// // Inserindo Capítulos
		//
		// int iCapitulos = gerarClassesCapitulos(conn);
		// System.out.println("Classes Criadas para Capítulos" + iCapitulos);
		//
		// FileOutputStream output = new FileOutputStream(
		// "/home/04295115401/Documentos/MESTRTADO/Arquivos CID-10/ntdoModificada/CID-10.owl");
		// model.write(output, "RDF/XML-ABBREV");
		//
		// output.close();
		//
		// // Inserindo Grupos
		//
		// int iGrupos = gerarClassesGrupos(conn);
		// System.out.println("Classes Criadas para Grupos" + iGrupos);
		//
		// FileOutputStream output2 = new FileOutputStream(
		// "/home/04295115401/Documentos/MESTRTADO/Arquivos CID-10/ntdoModificada/CID-10.owl");
		// model.write(output2, "RDF/XML-ABBREV");
		//
		// output2.close();
		//
		// // Inserindo Categorias
		//
		// int iCategorias = gerarClassesCategorias(conn);
		// System.out.println("Classes Criadas para Categorias" + iCategorias);
		//
		// FileOutputStream output3 = new FileOutputStream(
		// "/home/04295115401/Documentos/MESTRTADO/Arquivos CID-10/ntdoModificada/CID-10.owl");
		// model.write(output3, "RDF/XML-ABBREV");
		//
		// output3.close();
		//
		// System.out.println("Iniciando classes disjuntas ...");

		// incluirClassesDisjuntasCategorias();
		// incluirClassesDisjuntasCategorias2();

		// System.out.println("Finalizando classes disjuntas ...");
		//
		// output3 = new FileOutputStream(
		// "/home/04295115401/Documentos/MESTRTADO/Arquivos CID-10/ntdoModificadaModificada/CID-10.owl");
		// model.write(output3, "RDF/XML-ABBREV");
		//
		// output3.close();

		// Inserindo Subcategorias

		int iSubcategorias = gerarClassesSubcategorias(conn);
		System.out.println("Classes Criadas para Subcategorias"
				+ iSubcategorias);

		FileOutputStream output4 = new FileOutputStream(
				"/home/04295115401/Documentos/MESTRTADO/Arquivos CID-10/ntdoModificada/CID-10.owl");
		model.write(output4, "RDF/XML-ABBREV");

		output4.close();

		// incluirClassesDisjuntasSubcategorias();

		// incluirClassesDisjuntas();

		// incluirClassesDisjuntasGrupos();

		// incluirClassesDisjuntasSubcategorias();
		//
		// FileOutputStream output4 = new FileOutputStream(
		// "/home/04295115401/Documentos/MESTRTADO/Arquivos CID-10/ntdoModificada/CID-10.owl");
		// model.write(output4, "RDF/XML-ABBREV");
		//
		// output4.close();

		System.out.println(">> Finalizando Carregando a ontologia >> ");

		GerarInstancias.closeConnection(conn);

	}

	/****************************************************************************
	 * 
	 * ------------------------------CAPÍTULOS----------------------------------
	 * -
	 * 
	 ****************************************************************************/

	private static int gerarClassesCapitulos(Connection conn)
			throws SQLException, FileNotFoundException, ClassNotFoundException {
		PreparedStatement prep = conn
				.prepareStatement("select * from capitulos order by numcap");
		ResultSet result = prep.executeQuery();

		GerarInstancias
				.loadOntology("/home/04295115401/Documentos/MESTRTADO/Arquivos CID-10/ntdoModificada/CID-10.owl");

		int i = 0;

		while (result.next()) {
			GerarInstancias.alterarOntologiasIncluindoCapitulos(
					result.getString("numcap"), result.getString("catinic"),
					result.getString("catfim"), result.getString("descricao"),
					result.getString("descrabrev"));

			i++;
		}

		System.out.println("Classes disjuntas");

		// prep =
		// conn.prepareStatement("select * from capitulos order by numcap");
		// result = prep.executeQuery();
		//
		// while (result.next()) {
		// incluir restrição de classes disjuntas

		// OntClass classe = GerarInstancias.model
		// .getOntClass("http://www.cin.ufpe.br/~ntdo/CID-10.owl#Capitulo_"
		// + result.getString("catinic")
		// + result.getString("catfim"));
		incluirClassesDisjuntas();
		// }

		return i;
	}

	private static void alterarOntologiasIncluindoCapitulos(String numcap,
			String catinic, String catfim, String descricao, String descrabrev)
			throws FileNotFoundException, SQLException, ClassNotFoundException {

		OntClass classe = GerarInstancias.model
				.createClass("http://www.cin.ufpe.br/~ntdo/CID-10.owl#Capitulo_"
						+ catinic + catfim);

		OntClass classeBiotopCondition = GerarInstancias.model
				.getOntClass("http://purl.org/biotop/biotop.owl#Condition");
		classe.addSuperClass(classeBiotopCondition);

		Literal labels = GerarInstancias.model.createLiteral(descricao);
		classe.addLabel(labels);

		// Literal comentarios =
		// GerarInstancias.model.createLiteral(descrabrev);
		// classe.addComment(comentarios);

	}

	/**
	 * Para cada classe de capítulo, gerar as restrições sobre classes
	 * disjuntas.
	 * 
	 * @param classe
	 * @param numcap
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 * @throws FileNotFoundException
	 */
	private static void incluirClassesDisjuntas() throws SQLException,
			ClassNotFoundException, FileNotFoundException {
		// Informando as Classes Disjuntas
		Connection conn = GerarInstancias.getConnection();

		GerarInstancias
				.loadOntology("/home/04295115401/Documentos/MESTRTADO/Arquivos CID-10/ntdoModificada/CID-10.owl");

		PreparedStatement prep = conn
				.prepareStatement("select numcap, catinic, catfim from capitulos order by numcap");
		ResultSet result = prep.executeQuery();

		List<RDFNode> nos = new ArrayList<RDFNode>();

		while (result.next()) {
			OntClass classe = GerarInstancias.model
					.getOntClass("http://www.cin.ufpe.br/~ntdo/CID-10.owl#Capitulo_"
							+ result.getString("catinic")
							+ result.getString("catfim"));
			nos.add(classe);
		}

		RDFList lista = GerarInstancias.model.createList(nos.iterator());

		GerarInstancias.model.createAllDifferent(lista);

	}

	/**
	 * Para cada classe de capítulo, gerar as restrições sobre classes
	 * disjuntas.
	 * 
	 * @param classe
	 * @param numcap
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	// private static void incluirClassesDisjuntas(OntClass classe, String
	// numcap)
	// throws SQLException, ClassNotFoundException {
	// // Informando as Classes Disjuntas
	// Connection conn = GerarInstancias.getConnection();
	//
	// PreparedStatement prep = conn
	// .prepareStatement("select numcap, catinic, catfim from capitulos where numcap not in ('"
	// + numcap + "') order by numcap");
	// ResultSet result = prep.executeQuery();
	//
	// RDFList lista = GerarInstancias.model.createList(arg0);
	//
	// while (result.next()) {
	// OntClass classe2 = GerarInstancias.model
	// .getOntClass("http://www.cin.ufpe.br/~ntdo/CID-10.owl#Capitulo_"
	// + result.getString("catinic")
	// + result.getString("catfim"));
	//
	// classe.asAllDifferent();
	// }
	//
	// RDFNode noclassesdisjuntas =
	// GerarInstancias.model.createAllDifferent(arg0);
	//
	// }

	/****************************************************************************
	 * 
	 * --------------------------------GRUPOS----------------------------------
	 * ---
	 * 
	 ****************************************************************************/

	private static int gerarClassesGrupos(Connection conn) throws SQLException,
			FileNotFoundException, ClassNotFoundException {

		PreparedStatement prep = conn
				.prepareStatement("select * from capitulos order by numcap");
		ResultSet result = prep.executeQuery();

		int i = 0;

		while (result.next()) {

			PreparedStatement prep2 = conn
					.prepareStatement("select * from grupos where catinic between '"
							+ result.getString("catinic")
							+ "' and '"
							+ result.getString("catfim")
							+ "' and catfim between '"
							+ result.getString("catinic")
							+ "' and '"
							+ result.getString("catfim") + "'");
			ResultSet result2 = prep2.executeQuery();

			GerarInstancias
					.loadOntology("/home/04295115401/Documentos/MESTRTADO/Arquivos CID-10/ntdoModificada/CID-10.owl");

			while (result2.next()) {
				GerarInstancias
						.alterarOntologiasIncluindoGrupos(
								result2.getString("catinic"),
								result2.getString("catfim"),
								result2.getString("descricao"),
								result2.getString("descrabrev"),
								result.getString("catinic"),
								result.getString("catfim"));

				i++;
			}

		}

		// prep =
		// conn.prepareStatement("select * from capitulos order by numcap");
		// result = prep.executeQuery();

		// while (result.next()) {
		//
		// PreparedStatement prep2 = conn
		// .prepareStatement("select * from grupos where catinic between '"
		// + result.getString("catinic")
		// + "' and '"
		// + result.getString("catfim")
		// + "' and catfim between '"
		// + result.getString("catinic")
		// + "' and '"
		// + result.getString("catfim") + "'");
		// ResultSet result2 = prep2.executeQuery();
		//
		// while (result2.next()) {
		// // incluir restrição de classes disjuntas
		//
		// OntClass classe = GerarInstancias.model
		// .getOntClass("http://www.cin.ufpe.br/~ntdo/CID-10.owl#Grupo_"
		// + result2.getString("catinic")
		// + result2.getString("catfim"));
		// incluirClassesDisjuntasGrupos(classe,
		// result.getString("catinic"),
		// result.getString("catfim"),
		// result2.getString("catinic"),
		// result2.getString("catfim"));
		// }

		incluirClassesDisjuntasGrupos();

		return i;
	}

	private static void alterarOntologiasIncluindoGrupos(String catinic,
			String catfim, String descricao, String descrabrev,
			String catinicc, String catfimc) throws FileNotFoundException {

		OntClass classe = GerarInstancias.model
				.createClass("http://www.cin.ufpe.br/~ntdo/CID-10.owl#Grupo_"
						+ catinic + catfim);

		OntClass classeBiotopCondition = GerarInstancias.model
				.getOntClass("http://www.cin.ufpe.br/~ntdo/CID-10.owl#Capitulo_"
						+ catinicc + catfimc);
		classe.addSuperClass(classeBiotopCondition);

		Literal labels = GerarInstancias.model.createLiteral(descricao);
		classe.addLabel(labels);

		Literal comentarios = GerarInstancias.model.createLiteral(descrabrev);
		classe.addComment(comentarios);

	}

	/**
	 * Para cada classe de grupo, gerar as restrições sobre classes disjuntas.
	 * 
	 * @param classe
	 * @param numcap
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 * @throws FileNotFoundException
	 */
	private static void incluirClassesDisjuntasGrupos() throws SQLException,
			ClassNotFoundException, FileNotFoundException {
		// Informando as Classes Disjuntas
		// Informando as Classes Disjuntas
		Connection conn = GerarInstancias.getConnection();

		GerarInstancias
				.loadOntology("/home/04295115401/Documentos/MESTRTADO/Arquivos CID-10/ntdoModificada/CID-10.owl");

		PreparedStatement prep = conn
				.prepareStatement("select numcap, catinic, catfim from capitulos order by numcap");
		ResultSet result2 = prep.executeQuery();

		int i = 0;

		while (result2.next()) {

			OntClass classe = GerarInstancias.model
					.getOntClass("http://www.cin.ufpe.br/~ntdo/CID-10.owl#Capitulo_"
							+ result2.getString("catinic")
							+ result2.getString("catfim"));

			Iterator<OntClass> classes = classe.listSubClasses(false);

			List<RDFNode> nos = new ArrayList<RDFNode>();

			while (classes.hasNext()) {
				nos.add(classes.next());
			}

			RDFList lista = GerarInstancias.model.createList(nos.iterator());

			GerarInstancias.model.createAllDifferent(lista);

		}
	}

	/****************************************************************************
	 * 
	 * --------------------------------CATEGORIAS------------------------------
	 * --
	 * 
	 ****************************************************************************/

	private static int gerarClassesCategorias(Connection conn)
			throws SQLException, FileNotFoundException, ClassNotFoundException {

		PreparedStatement prep = conn
				.prepareStatement("select catinic, catfim from grupos");
		ResultSet result = prep.executeQuery();

		int i = 0;

		GerarInstancias
				.loadOntology("/home/04295115401/Documentos/MESTRTADO/Arquivos CID-10/ntdoModificada/CID-10.owl");

		System.out.println("Iniciando alteração das categorias ...");

		while (result.next()) {

			PreparedStatement prep2 = conn
					.prepareStatement("select cat,descricao, descrabrev from categorias where cat between '"
							+ result.getString("catinic")
							+ "' and '"
							+ result.getString("catfim") + "'");
			ResultSet result2 = prep2.executeQuery();

			while (result2.next()) {
				GerarInstancias
						.alterarOntologiasIncluindoCategorias(
								result2.getString("cat"),
								result2.getString("descricao"),
								result2.getString("descrabrev"),
								result.getString("catinic"),
								result.getString("catfim"));

				i++;
			}

		}

		System.out.println("Finalizando alteração das categorias ...");

		return i;
	}

	private static void alterarOntologiasIncluindoCategorias(String cat,
			String descricao, String descrabrev, String catinic, String catfim)
			throws FileNotFoundException {

		OntClass classe = GerarInstancias.model
				.createClass("http://www.cin.ufpe.br/~ntdo/CID-10.owl#Categoria_"
						+ cat);

		OntClass classeBiotopCondition = GerarInstancias.model
				.getOntClass("http://www.cin.ufpe.br/~ntdo/CID-10.owl#Grupo_"
						+ catinic + catfim);
		classe.addSuperClass(classeBiotopCondition);

		Literal labels = GerarInstancias.model.createLiteral(descricao);
		classe.addLabel(labels);

		// Literal comentarios =
		// GerarInstancias.model.createLiteral(descrabrev);
		// classe.addComment(comentarios);

	}

	/**
	 * Para cada classe de categoria, gerar as restrições sobre classes
	 * disjuntas.
	 * 
	 * @param classe
	 * @param numcap
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 * @throws FileNotFoundException
	 */
	private static void incluirClassesDisjuntasCategorias()
			throws SQLException, ClassNotFoundException, FileNotFoundException {
		// Informando as Classes Disjuntas
		Connection conn = GerarInstancias.getConnection();

		GerarInstancias
				.loadOntology("/home/04295115401/Documentos/MESTRTADO/Arquivos CID-10/ntdoModificada/CID-10.owl");

		PreparedStatement prep = conn
				.prepareStatement("select catinic, catfim from grupos");
		ResultSet result = prep.executeQuery();

		while (result.next()) {

			OntClass classe = GerarInstancias.model
					.getOntClass("http://www.cin.ufpe.br/~ntdo/CID-10.owl#Grupo_"
							+ result.getString("catinic")
							+ result.getString("catfim"));

			Iterator<OntClass> classes = classe.listSubClasses();

			List<RDFNode> nos = new ArrayList<RDFNode>();

			while (classes.hasNext()) {
				nos.add(classes.next());
			}

			RDFList lista = GerarInstancias.model.createList(nos.iterator());
			GerarInstancias.model.createAllDifferent(lista);
		}

	}

	private static void incluirClassesDisjuntasCategorias2()
			throws SQLException, ClassNotFoundException, FileNotFoundException {
		// Informando as Classes Disjuntas
		Connection conn = GerarInstancias.getConnection();

		GerarInstancias
				.loadOntology("/home/04295115401/Documentos/MESTRTADO/Arquivos CID-10/ntdoModificada/CID-10.owl");

		PreparedStatement prep = conn
				.prepareStatement("select catinic, catfim from grupos");
		ResultSet result = prep.executeQuery();

		List<RDFNode> nos = new ArrayList<RDFNode>();

		while (result.next()) {

			OntClass classe = GerarInstancias.model
					.getOntClass("http://www.cin.ufpe.br/~ntdo/CID-10.owl#Grupo_"
							+ result.getString("catinic")
							+ result.getString("catfim"));

			Iterator<OntClass> classes = classe.listSubClasses();

			while (classes.hasNext()) {
				nos.add(classes.next());
			}
		}

		RDFList lista = GerarInstancias.model.createList(nos.iterator());

		GerarInstancias.model.createAllDifferent(lista);

	}

	/****************************************************************************
	 * 
	 * --------------------------------SUBCATEGORIAS----------------------------
	 * 
	 ****************************************************************************/

	private static int gerarClassesSubcategorias(Connection conn)
			throws SQLException, FileNotFoundException, ClassNotFoundException {

		GerarInstancias
				.loadOntology("/home/04295115401/Documentos/MESTRTADO/Arquivos CID-10/ntdoModificada/CID-10.owl");

		PreparedStatement prep = conn
				.prepareStatement("select cat from categorias");
		ResultSet result = prep.executeQuery();

		int i = 0;

		while (result.next()) {

			String cat = result.getString("cat");

			OntClass classeCategoriaPai = GerarInstancias.model
					.getOntClass("http://www.cin.ufpe.br/~ntdo/CID-10.owl#Categoria_"
							+ cat);

			PreparedStatement prep2 = conn
					.prepareStatement("select subcat, restrsexo, descricao, causaobito, descrabrev, refer "
							+ " from subcategorias where SUBSTR(subcat, 1, 3) = '"
							+ cat + "' ");
			ResultSet result2 = prep2.executeQuery();

			GerarInstancias
					.loadOntology("/home/04295115401/Documentos/MESTRTADO/Arquivos CID-10/ntdoModificada/CID-10.owl");

			while (result2.next()) {
				GerarInstancias.alterarOntologiasIncluindoSubcategorias(
						result2.getString("subcat"),
						result2.getString("descricao"),
						result2.getString("descrabrev"), classeCategoriaPai,
						result2.getString("restrsexo"),
						result2.getString("causaobito"),
						result2.getString("refer"));

				i++;
			}

		}

		return i;

	}

	private static void alterarOntologiasIncluindoSubcategorias(String subcat,
			String descricao, String descrabrev, OntClass classeCategoriaPai,
			String restrsexo, String causaobito, String refer)
			throws FileNotFoundException {

		OntClass classe = GerarInstancias.model
				.createClass("http://www.cin.ufpe.br/~ntdo/CID-10.owl#Subcategoria_"
						+ subcat);

		classe.addSuperClass(classeCategoriaPai);

		Literal labels = GerarInstancias.model.createLiteral(descricao);
		classe.addLabel(labels);

		Literal comentarios = GerarInstancias.model.createLiteral(descrabrev);
		classe.addComment(comentarios);

		OntClass human = GerarInstancias.model
				.getOntClass("http://purl.org/biotop/biotop.owl#Human");

		ObjectProperty hasPatient = GerarInstancias.model
				.getObjectProperty("http://purl.org/biotop/biotop.owl#hasPatient");

		AllValuesFromRestriction restricaoHasPatient = GerarInstancias.model
				.createAllValuesFromRestriction(null, hasPatient, human);

		classe.addSuperClass(restricaoHasPatient); 

		// criarRestricoesSubcategorias(
		// "http://www.cin.ufpe.br/~ntdo/CID-10.owl#Subcategoria_"
		// + subcat, classe, restrsexo, causaobito, refer,
		// classeCategoriaPai);

	}

	private static void criarRestricoesSubcategorias(String classes,
			OntClass classe, String restrsexo, String causaobito, String refer,
			OntClass classeCategoriaPai) {
		// 'subcat', 'restrsexo', 'causaobito', 'descricao', 'descrabrev' eh
		// 'refer'
		// OntClass biotopCondition = GerarInstancias.model
		// .getOntClass("http://purl.org/biotop/biotop.owl#Condition");

		List<RDFNode> nos = new ArrayList<RDFNode>();

		// nos.add(classe.getSuperClass());
		// nos.add(restricaoHasPatient);

		// Se possuir sexo feminino female
		if (restrsexo != null && restrsexo.equalsIgnoreCase("F")) {
			OntClass female = GerarInstancias.model
					.getOntClass("http://purl.org/obo/owl/PATO#PATO_0000383");

			ObjectProperty bearerOf = GerarInstancias.model
					.getObjectProperty("http://purl.org/biotop/biotop.owl#bearerOf");

			HasValueRestriction restricaoHasSexo = GerarInstancias.model
					.createHasValueRestriction(null, bearerOf, female);

			nos.add(restricaoHasSexo);

		} else if (restrsexo != null && restrsexo.equalsIgnoreCase("M")) {

			OntClass female = GerarInstancias.model
					.getOntClass("http://purl.org/obo/owl/PATO#PATO_0000384");

			ObjectProperty bearerOf = GerarInstancias.model
					.getObjectProperty("http://purl.org/biotop/biotop.owl#bearerOf");

			HasValueRestriction restricaoHasSexo = GerarInstancias.model
					.createHasValueRestriction(null, bearerOf, female);

			nos.add(restricaoHasSexo);

		}

		if (causaobito != null && causaobito.equalsIgnoreCase("N")) {

			OntClass deathProcess = GerarInstancias.model
					.getOntClass("http://www.cin.ufpe.br/~ntdo/OWLFiles/InjuryAndDeath.owl#DeathProcess");

			ObjectProperty causes = GerarInstancias.model
					.getObjectProperty("http://purl.org/biotop/biotop.owl#causes");

			SomeValuesFromRestriction restricaoNoSomeRestriction = GerarInstancias.model
					.createSomeValuesFromRestriction(null, causes, deathProcess);

			ComplementClass ukIndustrialConf = GerarInstancias.model
					.createComplementClass(null, restricaoNoSomeRestriction);
			nos.add(ukIndustrialConf);

		}

		GerarInstancias.model.createIntersectionClass(classes,
				GerarInstancias.model.createList(nos.iterator()));

	}

	private static void incluirClassesDisjuntasSubcategorias()
			throws SQLException, ClassNotFoundException, FileNotFoundException {
		// Informando as Classes Disjuntas
		Connection conn = GerarInstancias.getConnection();

		GerarInstancias
				.loadOntology("/home/04295115401/Documentos/MESTRTADO/Arquivos CID-10/ntdoModificada/CID-10.owl");

		PreparedStatement prep2 = conn
				.prepareStatement("select cat from categorias");
		ResultSet result2 = prep2.executeQuery();

		int i = 0;

		while (result2.next()) {

			OntClass classe = GerarInstancias.model
					.createClass("http://www.cin.ufpe.br/~ntdo/CID-10.owl#Categoria_"
							+ result2.getString("cat"));

			Iterator<OntClass> classes = classe.listSubClasses();

			List<RDFNode> nos = new ArrayList<RDFNode>();

			while (classes.hasNext()) {
				nos.add(classes.next());
			}

			RDFList lista = GerarInstancias.model.createList(nos.iterator());

			if (lista.size() > 1) {
				GerarInstancias.model.createAllDifferent(lista);
			}

		}

	}

	private static void loadOntology(String url) throws FileNotFoundException {

		if (GerarInstancias.model == null) {
			GerarInstancias.model = ModelFactory.createOntologyModel();

			GerarInstancias.model.read(new FileInputStream(url), "");
		}
	}

	private static Connection getConnection() throws SQLException,
			ClassNotFoundException {

		Class.forName("org.postgresql.Driver");
		Connection conn = DriverManager.getConnection(
				"jdbc:postgresql://localhost:5432/postgres", "postgres",
				"postgres");

		return conn;
	}

	private static void closeConnection(Connection conn) throws SQLException {
		conn.close();
	}

}

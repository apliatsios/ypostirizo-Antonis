package com.mycompany.mavenproject1;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.impl.TreeModel;
import org.eclipse.rdf4j.model.util.GraphUtil;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.query.Update;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.config.RepositoryConfig;
import org.eclipse.rdf4j.repository.config.RepositoryConfigSchema;
import org.eclipse.rdf4j.repository.http.HTTPRepository;
import org.eclipse.rdf4j.repository.manager.LocalRepositoryManager;
import org.eclipse.rdf4j.repository.manager.RepositoryManager;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFParser;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.rio.helpers.StatementCollector;

/**
 *
 * @author apliatsios
 */
public class AntonisDemo {

    private static final String GRAPHDB_SERVER = "http://localhost:7200/";
    private static final String REPOSITORY_ID = "ypostirizo1";
    public static String filename2 = "ontology.rdf";

    public static void main(String[] args) throws IOException {
// GraphDB 

        // Instantiate a local repository manager and initialize it
        RepositoryManager repositoryManager = new LocalRepositoryManager(new File("$GraphDBInstall/data/repositories"));
        repositoryManager.initialize();

// Instantiate a repository graph model
        TreeModel graph = new TreeModel();

// Read repository configuration file
        InputStream config = AntonisDemo.class.getResourceAsStream("/ypostirizo1-config.ttl");
        RDFParser rdfParser = Rio.createParser(RDFFormat.TURTLE);
        rdfParser.setRDFHandler(new StatementCollector(graph));
        rdfParser.parse(config, RepositoryConfigSchema.NAMESPACE);
        config.close();

// Retrieve the repository node as a resource
        Resource repositoryNode = GraphUtil.getUniqueSubject(graph, RDF.TYPE, RepositoryConfigSchema.REPOSITORY);

// Create a repository configuration object and add it to the repositoryManager
        RepositoryConfig repositoryConfig = RepositoryConfig.create(graph, repositoryNode);
        repositoryManager.addRepositoryConfig(repositoryConfig);

// Get the repository from repository manager, note the repository id set in configuration .ttl file
        Repository repository = repositoryManager.getRepository("ypostirizo1");

// Open a connection to this repository
        RepositoryConnection repositoryConnection = repository.getConnection();

// ... use the repository
        try (InputStream input2
                = AntonisDemo.class.getResourceAsStream("/" + filename2)) {
            // add the RDF data from the inputstream directly to our database
            getRepositoryConnection().add(input2, "", RDFFormat.RDFXML);
        }
        String queryString = "PREFIX : <http://www.semanticweb.org/kass/ontologies/2020/2/yposthrizo_ontology#> \n";
        queryString += "INSERT { \n";
        queryString += "?p owl:hasSleepProblem   :insomnia.} \n";
        queryString += "WHERE { \n";
        queryString += "    ?p a :Person . \n";
        queryString += "   ?p :age ?a2 . \n";
        queryString += "     FILTER (?a2>74) \n";
        queryString += "}";

        Update operation = getRepositoryConnection().prepareUpdate(QueryLanguage.SPARQL, queryString);

        operation.execute();
        int x = 5;
        try (InputStream input2
                = AntonisDemo.class.getResourceAsStream("/" + filename2)) {
            // add the RDF data from the inputstream directly to our database
            getRepositoryConnection().add(input2, "", RDFFormat.RDFXML);
        }
        String queryString2 = "PREFIX : <http://www.semanticweb.org/kass/ontologies/2020/2/yposthrizo_ontology#> \n";
        queryString2 += "INSERT { \n";
        queryString2 += "?p :age ";
        queryString2 += String.valueOf(x);
        queryString2 += " } \n";
        queryString2 += "WHERE { \n";
        queryString2 += "    ?p a :Person . \n";
        queryString2 += "   ?p :age ?a2 . \n";
        queryString2 += "     FILTER (?a2>150) \n";
        queryString2 += "}";
        Update operation2 = getRepositoryConnection().prepareUpdate(QueryLanguage.SPARQL, queryString2);

        operation2.execute();
//TupleQuery query = getRepositoryConnection().prepareTupleQuery(queryString);

        //	TupleQueryResult result = query.evaluate();
        //                     System.out.println("hello");
        //			while (result.hasNext()) {
        //				BindingSet solution = result.next();
        // ... and print out the value of the variable binding for ?s and ?n
        //				System.out.println("?p = " + solution.getValue("p"));
        //                               System.out.println("hello");
        //	}
        //	}
// Shutdown connection, repository and manager
        repositoryConnection.close();
        repository.shutDown();
        repositoryManager.shutDown();
    }

    private static RepositoryConnection getRepositoryConnection() {
        Repository repository = new HTTPRepository(GRAPHDB_SERVER, REPOSITORY_ID);
        repository.initialize();
        RepositoryConnection repositoryConnection = repository.getConnection();
        return repositoryConnection;
    }
}

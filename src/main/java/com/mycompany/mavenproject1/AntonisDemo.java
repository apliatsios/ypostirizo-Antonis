package com.mycompany.mavenproject1;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.model.impl.TreeModel;
import org.eclipse.rdf4j.model.util.GraphUtil;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.config.RepositoryConfig;
import org.eclipse.rdf4j.repository.config.RepositoryConfigSchema;
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
    public static void main(String[] args) throws IOException {

		String filename = "yposthrizo.spin.ttl";

		// read the file 'example-data-artists.ttl' as an InputStream.
		InputStream input = AntonisDemo.class.getResourceAsStream("/" + filename);

		// Rio also accepts a java.io.Reader as input for the parser.
		Model model = Rio.parse(input, "", RDFFormat.TURTLE);

		// To check that we have correctly read the file, let's print out the model to the screen again
		for (Statement statement: model) {
			System.out.println(statement);
		}
                
		RepositoryManager repositoryManager = new LocalRepositoryManager(new File("."));
repositoryManager.initialize();

// Instantiate a repository graph model
TreeModel graph = new TreeModel();

// Read repository configuration file
//InputStream config = AntonisDemo.class.getResourceAsStream("/repo-defaults.ttl");
InputStream config = AntonisDemo.class.getResourceAsStream("/test_antonis-config.ttl");
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
Repository repository = repositoryManager.getRepository("test_antonis");

// Open a connection to this repository
RepositoryConnection repositoryConnection = repository.getConnection();

// ... use the repository

// Shutdown connection, repository and manager
repositoryConnection.close();
repository.shutDown();
repositoryManager.shutDown();                
                
                
                
}}

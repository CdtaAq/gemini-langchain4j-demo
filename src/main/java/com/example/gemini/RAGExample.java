package com.example.gemini;

import dev.langchain4j.model.embedding.VertexAiEmbeddingModel;
import dev.langchain4j.model.vertexai.VertexAiGeminiChatModel;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.InMemoryEmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.retriever.EmbeddingStoreRetriever;
import dev.langchain4j.chain.Chain;

import java.nio.file.Paths;
import java.util.List;

public class RAGExample {

    public static void main(String[] args) {
        // Embedding model
        VertexAiEmbeddingModel embeddingModel = VertexAiEmbeddingModel.builder()
                .project(System.getenv("GOOGLE_CLOUD_PROJECT"))
                .location("us-central1")
                .modelName("textembedding-gecko@003")
                .build();

        // Embedding store (in-memory for demo; use PGVector, Pinecone, etc. in prod)
        InMemoryEmbeddingStore<TextSegment> store = new InMemoryEmbeddingStore<>();

        // Load documents (txt/pdf supported)
        Document doc = FileSystemDocumentLoader.loadDocument(Paths.get("docs/sample.txt"));
        EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
                .embeddingModel(embeddingModel)
                .embeddingStore(store)
                .build();
        ingestor.ingest(doc);

        // Setup retriever
        EmbeddingStoreRetriever<TextSegment> retriever = new EmbeddingStoreRetriever<>(store, embeddingModel);

        // Chat model
        VertexAiGeminiChatModel chatModel = VertexAiGeminiChatModel.builder()
                .project(System.getenv("GOOGLE_CLOUD_PROJECT"))
                .location("us-central1")
                .modelName("gemini-1.5-pro")
                .build();

        // Create a QA chain
        Chain<String, String> ragChain = Chain.fromRetriever(chatModel, retriever);

        // Ask question grounded in document
        String answer = ragChain.execute("What does the document say about Java?");
        System.out.println("Gemini (RAG): " + answer);
    }
}

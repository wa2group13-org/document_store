package it.polito.wa2.g13.document_store

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DocumentStoreApplication {
//    @Bean
//    fun commandLineRunner(repo: DocumentRepository): CommandLineRunner {
//        return CommandLineRunner {
//            repo.save(
//                DocumentMetadata(
//                    0,
//                    "asdfklj",
//                    10,
//                    "SIUM",
//                    Calendar.getInstance().time,
//                    DocumentFile(0, ByteArray(10))
//                )
//            )
//            repo.findAll().toList().forEach { println(it) }
//        }
//    }
}

fun main(args: Array<String>) {
    runApplication<DocumentStoreApplication>(*args)
}

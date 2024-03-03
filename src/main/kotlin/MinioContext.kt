package app

import app.common.IMinioRepo
import org.slf4j.Logger
import org.slf4j.LoggerFactory

data class MinioContext(
    var minioRepo: IMinioRepo = IMinioRepo.NONE,
    val logger: Logger = LoggerFactory.getLogger("APP"),

    val bucketName: String = "demo-bucket",
    val dir: String = "/tmp/demo/",
    val dirDownload: String = "/tmp/download/",


    )
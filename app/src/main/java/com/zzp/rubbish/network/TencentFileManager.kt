package com.zzp.rubbish.network

import android.net.Uri
import com.tencent.cos.xml.CosXmlService
import com.tencent.cos.xml.CosXmlServiceConfig
import com.tencent.cos.xml.listener.CosXmlResultListener
import com.tencent.cos.xml.transfer.TransferConfig
import com.tencent.cos.xml.transfer.TransferManager
import com.tencent.qcloud.core.auth.QCloudCredentialProvider
import com.tencent.qcloud.core.auth.ShortTimeCredentialProvider
import com.zzp.rubbish.util.MyApplication.Companion.context


object TencentFileManager{
//    private val secretId = "AKIDXswjJcdU8Vgka9RTDgrlOjna6KHxMUMw" //永久密钥 secretId
    private val secretId = "AKIDJSLxJPEn44nGRLyLRGn33mQownMegRu6" //永久密钥 secretId
//    private val secretKey = "MREl0ye3B1astd5F80cw0mHUP3QWLs86" //永久密钥 secretKey
    private val secretKey = "wc07xWopZ4IFvKTLvEG1VieJhpgGZaKo" //永久密钥 secretKey
    // keyDuration 为请求中的密钥有效期，单位为秒
    private val myCredentialProvider: QCloudCredentialProvider =
        ShortTimeCredentialProvider(secretId, secretKey, 300)
    // 存储桶所在地域简称，例如广州地区是 ap-guangzhou
//    private const val region = "ap-shanghai"
    private const val region = "ap-nanjing"
    // 创建 CosXmlServiceConfig 对象，根据需要修改默认的配置参数
    private val serviceConfig: CosXmlServiceConfig = CosXmlServiceConfig.Builder()
        .setRegion(region)
        .isHttps(true) // 使用 HTTPS 请求, 默认为 HTTP 请求
        .builder()
    // 初始化 COS Service，获取实例
    private val cosXmlService = CosXmlService(
        context,
        serviceConfig, myCredentialProvider
    )

    // 初始化 TransferConfig，这里使用默认配置，如果需要定制，请参考 SDK 接口文档
    private var transferConfig = TransferConfig.Builder().build()

    // 初始化 TransferManager
    private var transferManager = TransferManager(
        cosXmlService,
        transferConfig
    )

    // 存储桶名称，由bucketname-appid 组成，appid必须填入，可以在COS控制台查看存储桶名称。 https://console.cloud.tencent.com/cos5/bucket
//    private var bucket = "zzp-garbage-1258505646"
    private var bucket = "garbage-1020-1310799117"
    private var cosPath = "garbage.jpg" //对象在存储桶中的位置标识符，即称对象键

    //srcPath: 本地文件的绝对路径
    fun uploadImage(phoneNum: String, byteArray: ByteArray, cosXmlResultListener: CosXmlResultListener) {
        // 上传文件
        val cosXmlUploadTask = transferManager.upload(bucket, "$phoneNum/$cosPath", byteArray)
        cosXmlUploadTask.setCosXmlResultListener(cosXmlResultListener)
    }

}
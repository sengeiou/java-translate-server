package com.cretin.translatetools

import com.cretin.translatetools.entity.TranslateDownloadBean
import com.cretin.translatetools.entity.TranslateUploadBean
import org.apache.http.util.TextUtils
import java.io.File
import java.lang.RuntimeException

/**
 * 转换帮助类
 */
object TranslateFormatHelper {

    //获取内容
    private fun getContent(line: String): String {
        if (line.contains("</string>"))
            return line.trim().replace("</string>", "").replace("<string name=", "")?.let {
                it.substring(it.indexOf(">") + 1)
            }
        return ""
    }

    /**
     * 获取文件包名
     */
    private fun getPackageName(file: File, rootAddress: String): String {
        return file.parent.replace(rootAddress, "")
    }

    //获取key     <string name="photo_title" >Turn on the camera</string>
    fun getKey(line: String): String {
        if (line.contains("</string>")) {
            line.trim().apply {
                this.substring(this.indexOf("\"") + 1, this.indexOf(">")).apply {
                    return this.substring(0,this.lastIndexOf("\""))
                }
            }
        }
        return ""
    }

    //获取key
    private fun getKeyIOS(line: String): String {
        if (line.trim().startsWith("\"") && line.trim().endsWith(";") && line.trim().contains("="))
            line.trim().split("=")?.apply {
                if (this.size == 2) {
                    this[0].trim().apply {
                        if (this.startsWith("\"") && this.endsWith("\"")) {
                            return this.substring(1, this.length - 1)
                        }
                    }
                }
            }
        return ""
    }

    //获取内容
    private fun getContentIOS(line: String): String {
        if (line.trim().startsWith("\"") && line.trim().endsWith(";") && line.trim().contains("="))
            line.trim().split("=")?.apply {
                if (this.size == 2) {
                    this[1].trim().apply {
                        if (this.endsWith(";")) {
                            this.substring(0, this.length - 1).trim().apply {
                                if (this.startsWith("\"") && this.endsWith("\"")) {
                                    return this.substring(1, this.length - 1)
                                }
                            }
                        }
                    }
                }
            }
        return ""
    }

    /**
     * 替换内容
     */
    private fun replaceContent(line: String, append: String): String {
        if (line.contains("</string>"))
            return line.substring(0, line.indexOf(">") + 1) + append + line.substring(line.indexOf("</string>"))
        return line
    }

    /**
     * 转换云端数据到本地
     *
     * projectRoot 项目地址
     */
    fun convertRemoteDataToLocal(downloadTranslate: TranslateDownloadBean?, projectAddress: String) {
        val projectRoot = projectAddress.trim()
        if (TextUtils.isEmpty(projectRoot)) throw RuntimeException("项目地址不能为空")
        val rootFile = File(projectRoot)
        if (!rootFile.exists()) throw RuntimeException("项目地址不存在")
        val fileTree = rootFile.walk()
        fileTree.maxDepth(100)//遍历目录层级为100
                .filter { it.path.endsWith("/values/strings.xml") }
                .filter { it.isFile } //只挑选出文件,不处理文件夹
                .forEach { //循环处理符合条件的文件
                    convertSingleFileToLocal(downloadTranslate, it, projectRoot)
                }
    }

    /**
     * 转换云端数据到本地
     *
     * projectRoot 项目地址
     */
    fun convertRemoteDataToLocalIOS(downloadTranslate: TranslateDownloadBean?, projectAddress: String): String {
        var projectRoot = projectAddress.trim()
        if (TextUtils.isEmpty(projectRoot)) throw RuntimeException("项目地址不能为空")
        while (projectRoot.endsWith("/")) {
            projectRoot = projectRoot.substring(0, projectRoot.length - 1)
        }
        if (projectRoot.endsWith("zh-Hans.lproj")) {
            //正确
            val rootFile = File(projectRoot)
            val originFile = File(projectRoot, "Localizable.strings")
            if (!originFile.exists()) throw RuntimeException("中文翻译文件不存在")
            if (!rootFile.exists()) throw RuntimeException("项目地址不存在")
            val newFile = File(projectRoot.replace("zh-Hans.lproj", "en.lproj"))
            if (!newFile.exists()) {
                newFile.mkdirs()
            }
            val aimFile = File(newFile.path, "Localizable.strings")
            if (aimFile.exists()) {
                //备份 2022年01月18日17:31:11 去掉备份逻辑 删除文件
//                aimFile.renameTo(File(newFile, "Localizable.strings-" + System.currentTimeMillis()))
                aimFile.delete()
            }
            aimFile.createNewFile()

            for (line in originFile.readLines()) {
                val realLine = line.trim()
                if (realLine.startsWith("\"") && realLine.endsWith(";") && realLine.contains("=")) {
                    //合法
                    //是标准的string数据
                    val key = getKeyIOS(line)
                    val item = downloadTranslate?.data?.items?.find { temp -> temp.key == key }
                    println(key + "  " + item?.translateEn)
                    if (item != null)
                        aimFile.appendText("\"$key\" = \"" + item?.translateEn + "\";\n")
                } else {
                    aimFile.appendText(line + "\n")
                }
            }

            return "生成的英文文件位于：" + aimFile.absolutePath
        } else {
            throw RuntimeException("项目地址必须以 zh-Hans.lproj 结尾")
        }
    }

    /**
     * 收集本地数据进行上传
     */
    fun convertLocalDataToRemote(projectAddress: String): String? {
        val projectRoot = projectAddress.trim()
        if (TextUtils.isEmpty(projectRoot)) throw RuntimeException("项目地址不能为空")
        val rootFile = File(projectRoot)
        if (!rootFile.exists()) throw RuntimeException("项目地址不存在")
        val fileTree = rootFile.walk()
        val list = mutableListOf<TranslateUploadBean.ItemsBean>()
        fileTree.maxDepth(100)//遍历目录层级为100
                .filter { it.path.endsWith("/values/strings.xml") }
                .filter { it.isFile } //只挑选出文件,不处理文件夹
                .forEach { //循环处理符合条件的文件
                    list.addAll(convertSingleFileToRemote(it, projectRoot))
                }
        val data = TranslateUploadBean()
        data.clientType = 0
        data.items = list
        return TranslateDataHelper.uploadTranslate(data)
    }

    /**
     * 收集本地英文数据进行上传
     */
    fun convertLocalEnDataToRemote(projectAddress: String): String? {
        val projectRoot = projectAddress.trim()
        if (TextUtils.isEmpty(projectRoot)) throw RuntimeException("项目地址不能为空")
        val rootFile = File(projectRoot)
        if (!rootFile.exists()) throw RuntimeException("项目地址不存在")
        val fileTree = rootFile.walk()
        val list = mutableListOf<TranslateUploadBean.ItemsBean>()
        fileTree.maxDepth(100)//遍历目录层级为100
                .filter { it.path.endsWith("/values-en/strings.xml") }
                .filter { it.isFile } //只挑选出文件,不处理文件夹
                .forEach { //循环处理符合条件的文件
                    list.addAll(convertSingleFileToRemote(it, projectRoot))
                }
        val data = TranslateUploadBean()
        data.clientType = 0
        data.items = list
        return TranslateDataHelper.uploadTranslate(data)
    }

    /**
     * 收集本地数据进行上传
     */
    fun preConvertLocalDataToRemote(projectAddress: String): TranslateUploadBean {
        val projectRoot = projectAddress.trim()
        if (TextUtils.isEmpty(projectRoot)) throw RuntimeException("项目地址不能为空")
        val rootFile = File(projectRoot)
        if (!rootFile.exists()) throw RuntimeException("项目地址不存在")
        val fileTree = rootFile.walk()
        val list = mutableListOf<TranslateUploadBean.ItemsBean>()
        fileTree.maxDepth(100)//遍历目录层级为100
                .filter { it.path.endsWith("/values/strings.xml") }
                .filter { it.isFile } //只挑选出文件,不处理文件夹
                .forEach { //循环处理符合条件的文件
                    list.addAll(convertSingleFileToRemote(it, projectRoot))
                }
        val data = TranslateUploadBean()
        data.clientType = 0
        data.items = list
        return data
    }

    /**
     * 收集本地英文数据进行上传
     */
    fun preConvertLocalEnDataToRemote(projectAddress: String): TranslateUploadBean {
        val projectRoot = projectAddress.trim()
        if (TextUtils.isEmpty(projectRoot)) throw RuntimeException("项目地址不能为空")
        val rootFile = File(projectRoot)
        if (!rootFile.exists()) throw RuntimeException("项目地址不存在")
        val fileTree = rootFile.walk()
        val list = mutableListOf<TranslateUploadBean.ItemsBean>()
        fileTree.maxDepth(100)//遍历目录层级为100
                .filter { it.path.endsWith("/values-en/strings.xml") }
                .filter { it.isFile } //只挑选出文件,不处理文件夹
                .forEach { //循环处理符合条件的文件
                    list.addAll(convertSingleFileToRemote(it, projectRoot))
                }
        val data = TranslateUploadBean()
        data.clientType = 0
        data.items = list
        return data
    }

    /**
     * 收集本地数据进行上传 IOS
     */
    fun preConvertLocalDataToRemoteIOS(projectPath: String): TranslateUploadBean {
        val projectRoot = projectPath.trim()
        if (TextUtils.isEmpty(projectRoot)) throw RuntimeException("项目地址不能为空")
        if (!projectRoot.endsWith("zh-Hans.lproj")) {
            throw RuntimeException("项目地址必须以 zh-Hans.lproj 结尾")
        }
        val rootFile = File(projectRoot)
        if (!rootFile.exists()) throw RuntimeException("项目地址不存在")
        val fileTree = rootFile.walk()
        val list = mutableListOf<TranslateUploadBean.ItemsBean>()
        fileTree.maxDepth(100)//遍历目录层级为100
                .filter { it.isFile } //只挑选出文件,不处理文件夹
                .forEach { //循环处理符合条件的文件
                    list.addAll(convertSingleFileToRemoteIOS(it, projectRoot))
                }
        val data = TranslateUploadBean()
        data.clientType = 1
        data.items = list
        return data
    }

    /**
     * 收集本地英文数据进行上传 IOS
     */
    fun preConvertLocalEnDataToRemoteIOS(projectPath: String): TranslateUploadBean {
        val projectRoot = projectPath.trim()
        if (TextUtils.isEmpty(projectRoot)) throw RuntimeException("项目地址不能为空")
        if (!projectRoot.endsWith("zh-Hans.lproj")) {
            throw RuntimeException("项目地址必须以 zh-Hans.lproj 结尾")
        }
        val rootFile = File(projectRoot)
        if (!rootFile.exists()) throw RuntimeException("项目地址不存在")
        val enRootFile = File(projectRoot.replace("zh-Hans.lproj", "en.lproj"))
        if (!enRootFile.exists()) throw RuntimeException("未在同级目录中找到en.lproj文件夹")
        val fileTree = enRootFile.walk()
        val list = mutableListOf<TranslateUploadBean.ItemsBean>()
        fileTree.maxDepth(100)//遍历目录层级为100
                .filter { it.isFile } //只挑选出文件,不处理文件夹
                .forEach { //循环处理符合条件的文件
                    list.addAll(convertSingleFileToRemoteIOS(it, projectRoot))
                }
        val data = TranslateUploadBean()
        data.clientType = 1
        data.items = list
        return data
    }

    /**
     * 从本地处理单个文件 手机文件中的数据
     */
    private fun convertSingleFileToRemoteIOS(file: File, projectRoot: String): MutableList<TranslateUploadBean.ItemsBean> {
        if (!file.exists()) return mutableListOf()
        val list = mutableListOf<TranslateUploadBean.ItemsBean>()
        for (line in file.readLines()) {
            val realLine = line.trim()
            if (realLine.startsWith("\"") && realLine.endsWith(";") && realLine.contains("=")) {
                //合法
                //是标准的string数据
                val key = getKeyIOS(line)
                val content = getContentIOS(line)
                val item = TranslateUploadBean.ItemsBean()
                item.content = content
                item.key = key
                item.packageName = getPackageName(file, projectRoot)
                list.add(item)
            }
        }
        return list
    }

    /**
     * 从本地处理单个文件 手机文件中的数据
     */
    private fun convertSingleFileToRemote(file: File, projectRoot: String): MutableList<TranslateUploadBean.ItemsBean> {
        if (!file.exists()) return mutableListOf()
        val list = mutableListOf<TranslateUploadBean.ItemsBean>()
        for (line in file.readLines()) {
            if (line.contains("</string>") && line.contains("name=")) {
                //是标准的string数据
                val key = getKey(line)
                val content = getContent(line)
                val item = TranslateUploadBean.ItemsBean()
                item.content = content
                item.key = key
                item.packageName = getPackageName(file, projectRoot)
                list.add(item)
            }
        }
        return list
    }

    /**
     * 从本地处理单个文件 生成其他语言版本
     */
    private fun convertSingleFileToLocal(downloadTranslate: TranslateDownloadBean?, file: File, projectRoot: String) {
        if (!file.exists()) return
        val enFile = File(file.parent + "-en/strings.xml")
        if (enFile.exists()) {
            //Users/cretin/code_project/codemao/octopuslibrary/app/src/main/res/values-en/strings.xml
            //如果存在说明之前有
            val pre = if (projectRoot.endsWith("/")) projectRoot.substring(0, projectRoot.length - 1) else projectRoot
            val end = file.path.replace(projectRoot, "")
            val aimFile = File("$pre/translate-cache$end"?.let {
                it.substring(0, it.length - "/strings.xml".length) + "-en"
            })
            if (!aimFile.exists()) {
                aimFile.mkdirs()
            }
            enFile.renameTo(File(aimFile.path + "/strings.xml-" + System.currentTimeMillis()))
        } else {
            File(file.parent + "-en")?.apply {
                if (!this.exists()) {
                    this.mkdirs()
                }
            }
        }

        //创建文件
        val newFile = File(enFile.path)
        if (newFile.exists()) {
            newFile.createNewFile()
        }

        //遍历文件
        for (line in file.readLines()) {
            if (line.contains("</string>") && line.contains("name=")) {
                //是字符串内容 看云端有没有翻译
                val key = getKey(line)
                val translate = downloadTranslate?.data?.items?.find { it.key == key }
                if (translate != null) {
                    //云端有翻译
                    val content = translate.translateEn.replace("'", "\\'").replace("\\\\'", "\\'")
                    newFile.appendText(replaceContent(line, content) + "\n")
                } else {
                    //云端没有翻译 忽略
                }
            } else {
                //不是 直接添加
                newFile.appendText(line + "\n")
            }
        }
    }
}

//fun main(args: Array<String>) {
////    val data = TranslateFormatHelper.preConvertLocalDataToRemoteIOS("/Users/cretin/Downloads/ios_translate/zh-Hans.lproj")
////    println(data)
////    val data = TranslateUploadBean()
////    data.clientType = 1
////    data.items = mutableListOf()
////    val itemsBean = TranslateUploadBean.ItemsBean()
////    itemsBean.key = "ori_tip_deny_recording_permission"
////    itemsBean.content = "拒绝录音权限了，无法正常使用声音控制"
////    itemsBean.packageName = ""
////    data.items.add(itemsBean)
////    TranslateDataHelper.uploadTranslate(data)
////    val datas = TranslateDataHelper.downloadTranslateIOS()
////    TranslateFormatHelper.convertRemoteDataToLocalIOS(datas, "/Users/cretin/Downloads/ios_translate/zh-Hans.lproj")
//
//    val data = TranslateFormatHelper.preConvertLocalEnDataToRemote("/Users/cretin/code_project/codemao/temp/codemao")
//    val data1 = TranslateFormatHelper.preConvertLocalDataToRemote("/Users/cretin/code_project/codemao/temp/codemao")
//    println(data)

//    println(TranslateFormatHelper.getKey("    <string name=\"before_day\">Days ago</string>"))
//    println(TranslateFormatHelper.getKey("    <string name=\"photo_title\" >Turn on the camera</string>"))
//    println(TranslateFormatHelper.getKey("    <string name=\"bcm_version_higher_current\">\\\" 该作品bcm版本高于当前app,请前往更新，或者下载编程猫Nemo\\\"</string>"))
//    println(TranslateFormatHelper.getKey("    <string name=\"miao_copy_text\"><![CDATA[]吧，复制这段文本，打开编程猫即可浏览，内容为:\$&]]></string>"))
//    println(TranslateFormatHelper.getKey("    <string name=\"my_test_txt\">\\\"我的-草稿箱 \\\"</string>"))
//}

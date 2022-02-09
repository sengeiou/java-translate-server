package com.cretin.translatetools

//fun getContent(line: String): String {
//    return line.trim().replace("</string>", "").replace("<string name=", "")?.let {
//        it.substring(it.indexOf(">") + 1)
//    }
//}

//fun replaceContent(line: String, append: String): String {
//    if (line.contains("</string>"))
//        return line.substring(0, line.indexOf("</string>")) + append + line.substring(line.indexOf("</string>"))
//    return line
//}

//fun main(args: Array<String>) {
//    val root = "/Users/cretin/code_project/codemao/octopuslibrary"
//
////    val downloadTranslate = TranslateDataHelper.downloadTranslate()
////    TranslateFormatHelper.convertRemoteDataToLocal(downloadTranslate,root)
//
//    val log = TranslateFormatHelper.convertLocalDataToRemote(root)
//    println(log)
//
////    val fileTree = File(root).walk()
////    val fileNames = mutableListOf<String>()
////    fileTree.maxDepth(100)//遍历目录层级为1，即无需检查子目录
////            .filter { it.isFile } //只挑选出文件,不处理文件夹
////            .filter { it.path.endsWith("/values/strings.xml") }
////            .forEach {//循环处理符合条件的文件
////                fileNames.add(it.path)
////            }
////
////    //复制文件
////    fileNames.forEach {
////        val file = File(it)
////        val enFile = File(file.parent + "-en/strings.xml")
////        if (enFile.exists()) {
////            //如果存在说明之前有
////            enFile.renameTo(File(root + "/temp/" + "values-en/" + System.currentTimeMillis() + "-strings.xml"))
////        }else {
////            File(file.parent + "-en")?.apply {
////                if(!this.exists()){
////                    this.mkdirs()
////                }
////            }
////        }
////
////        //创建文件
////        val newFile = File(enFile.path)
////        if(newFile.exists()){
////            newFile.createNewFile()
////        }
////
////        for (readLine in file.readLines()) {
////            newFile.appendText(replaceContent(readLine, "-english") + "\n", )
////        }
////
////    }
//
////    var size = 0
////    fileNames.forEach {
////        File(it).readLines().forEach {
////            if (it.trim().startsWith("<string")) {
////                it.trim()?.apply {
////                    val content = this.replace("</string>", "").replace("<string name=", "")?.let {
////                        it.substring(it.indexOf(">") + 1)
////                    }
////                    val key = this.substring(0, this.indexOf(content) - 2).replace("<string name=\"", "")
////
////                    println(this + "   " + key+"   "+content)
////                }
////                size++
////            }
////        }
////    }
////    println(size.toString() + "次")
//}
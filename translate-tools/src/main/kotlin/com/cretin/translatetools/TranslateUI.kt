package com.cretin.translatetools

import com.cretin.translatetools.config.Config
import com.cretin.translatetools.config.Config.Companion.CAN_UPLAOD_EN
import com.cretin.translatetools.entity.ConfigJson
import com.cretin.translatetools.entity.TranslateDownloadBean
import com.cretin.translatetools.entity.TranslateUploadBean
import com.cretin.translatetools.utils.GsonGet
import org.apache.http.util.TextUtils
import java.awt.Color
import java.awt.Desktop
import java.awt.event.FocusEvent
import java.awt.event.FocusListener
import java.io.File
import java.net.URI
import java.text.SimpleDateFormat
import java.util.*
import javax.swing.*

class TranslateUI : JFrame() {
    private val mWidth = 800
    private val mHeight = 525
    private val panel: JPanel
    private val buttonSelect: JButton
    private var buttonUploadEn: JButton? = null
    private val buttonUpload: JButton
    private val buttonUploadPre: JButton
    private val buttonDownload: JButton
    private val buttonDownloadPre: JButton
    private val buttonTranslate: JButton
    private val buttonClear: JButton
    private val jCheckBox0: JCheckBox
    private val jCheckBox: JCheckBox
    private val textField: JTextField
    private val tokenField: JTextField
    private val textArea: JTextArea
    private val jLabel: JLabel
    private val jsp: JScrollPane
    private var currPathString: String? = null/**/
    private val separator: String
    private val adminUrl = Config.HOST_ADMIN
    private var localToken = ""
    private var currentClientType: Int = 0

    private var hintText = "请输入您的专属登录令牌，没有请找管理员获取"

    //data
    private var uploadData: TranslateUploadBean? = null
    private var downData: TranslateDownloadBean? = null

    private fun getConfigFile(): File? {
        return try {
            var jarPath = System.getProperty("java.class.path")
            jarPath = jarPath.substring(0, jarPath.lastIndexOf("/"))
            val file = File("$jarPath/cm_translate_v2.conf")
            if (!file.exists()) {
                file.createNewFile()
            }
            file
        } catch (e: Exception) {
            null
        }
    }

    private fun getConfig(): ConfigJson {
        if (!Config.IS_DEBUG) {
            val file = getConfigFile()
            val content = file?.readText() ?: ""
            return if (TextUtils.isEmpty(content)) {
                ConfigJson.createNew()
            } else {
                try {
                    GsonGet.getGson().fromJson(content, ConfigJson::class.java)
                } catch (e: Throwable) {
                    ConfigJson.createNew()
                }
            }
        } else {
            return ConfigJson(1, "/Users/cretin/Downloads/ios_translate/zh-Hans.lproj", "43822f8e981141ec9bf322f5aa6bbacb")
        }
    }

    private fun writeConfigClient(clientType: Int) {
        if (!Config.IS_DEBUG)
            getConfig()?.apply {
                this.clientType = clientType
                getConfigFile()?.writeText(GsonGet.getGson().toJson(this))
            }
    }

    private fun writeConfigVersion(version: String) {
        if (!Config.IS_DEBUG)
            getConfig()?.apply {
                this.version = version
                getConfigFile()?.writeText(GsonGet.getGson().toJson(this))
            }
    }

    private fun writeConfigToken(token: String) {
        if (!Config.IS_DEBUG)
            getConfig()?.apply {
                this.token = token
                getConfigFile()?.writeText(GsonGet.getGson().toJson(this))
            }
    }

    private fun writeConfigRootPath(rootPath: String) {
        if (!Config.IS_DEBUG)
            getConfig()?.apply {
                this.rootPath = rootPath
                getConfigFile()?.writeText(GsonGet.getGson().toJson(this))
            }
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            TranslateUI()
        }
    }

    init {
        title = "阿念翻译同步助手"
        setSize(mWidth, mHeight)
        setLocationRelativeTo(null)
        isResizable = false
        defaultCloseOperation = EXIT_ON_CLOSE

        panel = JPanel()
        buttonSelect = JButton("选择本地项目地址")

        buttonUploadPre = JButton("预加载本地数据")
        buttonUpload = JButton("上传本地数据到云端")
        buttonDownloadPre = JButton("远程下载数据到本地")
        buttonDownload = JButton("合并远程数据到本地")
        buttonTranslate = JButton("访问翻译后台")
        buttonClear = JButton("清空控制台")
        jCheckBox0 = JCheckBox("Android项目")
        jCheckBox = JCheckBox("iOS项目")
        jLabel = JLabel("项目地址")
        if (CAN_UPLAOD_EN) {
            buttonUploadEn = JButton("上传本地英文翻译")
            buttonUploadEn?.setForeground(java.awt.Color.RED);
            textField = JTextField(30)
            tokenField = JTextField(27)
        } else {
            textField = JTextField(42)
            tokenField = JTextField(39)
        }

        tokenField.addFocusListener(object : FocusListener {
            override fun focusGained(e: FocusEvent?) {
                val temp: String = tokenField.text
                if (temp == hintText) {
                    tokenField.text = ""
                    tokenField.foreground = Color.BLACK
                }
            }

            override fun focusLost(e: FocusEvent?) {
                val temp: String = tokenField.text
                if (temp == "") {
                    tokenField.foreground = Color.GRAY
                    tokenField.text = hintText
                } else {
                    //有内容
                    localToken = temp.trim()
                    writeConfigToken(localToken)
                }
            }
        })
        textField.text = "请选择本地项目地址"
        textField.isEnabled = false
        textArea = JTextArea(24, 64)
        jsp = JScrollPane(textArea)
        jsp.verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
        textArea.lineWrap = true // 设置文本区的换行策略
        textArea.isEditable = false
        separator = System.getProperties().getProperty("file.separator")
        panel.add(jLabel)
        panel.add(textField)
        panel.add(jCheckBox0)
        panel.add(jCheckBox)
        if (CAN_UPLAOD_EN) {
            panel.add(buttonUploadEn)
        }
        panel.add(buttonSelect)
        panel.add(buttonUploadPre)
        panel.add(buttonUpload)
        panel.add(buttonDownloadPre)
        panel.add(buttonDownload)
        panel.add(JLabel("登录令牌"))
        panel.add(tokenField)
        panel.add(buttonClear)
        panel.add(buttonTranslate)
        panel.add(jsp)

        getConfig()?.apply {
            textField.text = this.rootPath
            currPathString = this.rootPath

            currentClientType = this.clientType
            localToken = this.token

            if (this.clientType == 0) {
                jCheckBox0.isSelected = true
                jCheckBox.isSelected = false
            } else {
                jCheckBox0.isSelected = false
                jCheckBox.isSelected = true
            }

            if (!TextUtils.isEmpty(localToken)) {
                tokenField.text = localToken
            }

            try {
                val localVersion = this.version.replace(".", "").replace("v","").toInt()
                val newVersion = Config.VERSION.replace(".", "").replace("v","").toInt()
                if (newVersion > localVersion) {
                    JOptionPane.showMessageDialog(contentPane, Config.UPDATE_TIPS, "当前版本更新内容",
                            JOptionPane.WARNING_MESSAGE);
                    writeConfigVersion(Config.VERSION)
                }
            } catch (e: Throwable) {

            }
        }

        // 讲述使用说明
        initText()
        add(panel)
        jCheckBox.addItemListener { e ->
            val jcb = e.item as JCheckBox // 将得到的事件强制转化为JCheckBox类
            if (jcb.isSelected) { // 判断是否被选择
                jCheckBox0.isSelected = false

                currentClientType = 1
                writeConfigClient(1)
            }
        }
        jCheckBox0.addItemListener { e ->
            val jcb = e.item as JCheckBox // 将得到的事件强制转化为JCheckBox类
            if (jcb.isSelected) { // 判断是否被选择
                jCheckBox.isSelected = false

                currentClientType = 0
                writeConfigClient(0)
            }
        }

        // 选择文件夹
        buttonSelect.addActionListener {
            val jfc = JFileChooser()
            jfc.fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
            jfc.showDialog(JLabel(), "选择文件夹")
            val file = jfc.selectedFile
            if (file != null) {
                appendText("已选择文件夹：${file.absolutePath}")
                currPathString = file.absolutePath
                textField.text = currPathString
                //写入文件
                writeConfigRootPath(currPathString ?: "")
            } else {
                appendText("取消选择文件夹......")
            }
        }

        buttonUploadEn?.addActionListener {
            if (TextUtils.isEmpty(currPathString)) {
                appendText("请先选择项目地址！")
                JOptionPane.showMessageDialog(contentPane, "请先选择项目地址", "系统信息",
                        JOptionPane.WARNING_MESSAGE);
                return@addActionListener
            }
            buttonUploadEn?.isEnabled = false
            appendText("正在收集数据中...")
            Thread {
                try {
                    val data = if (currentClientType == 0) TranslateFormatHelper.preConvertLocalEnDataToRemote(currPathString
                            ?: "") else TranslateFormatHelper.preConvertLocalEnDataToRemoteIOS(currPathString ?: "")
                    val map = mutableMapOf<String, Int>()
                    data.token = localToken
                    data.items.forEach {
                        if (map.containsKey(it.key)) {
                            map[it.key] = map[it.key]!! + 1
                        } else {
                            map[it.key] = 1
                        }
                    }
                    val tempMap = map.filter { it.value > 1 }
                    if (!tempMap.isEmpty()) {
                        val result = StringBuilder()
                        tempMap.forEach { t, u ->
                            result.append(t + " 出现了 " + u + " 次\n")
                        }
                        val newResult = result.substring(0, result.length - 2)
                        JOptionPane.showMessageDialog(contentPane, "系统检测到以下KEY中存在多次提交，请检查并删除重复KEY方可上传：\n" + newResult, "提交重复KEY",
                                JOptionPane.WARNING_MESSAGE);
                        appendText("收集完成，系统检测到以下KEY中存在多次提交，请检查并删除重复KEY：\n" + newResult)
                    } else {
                        appendText("共收集本地翻译 ${data.items.size} 条，已准备好上传!")
                        val select = JOptionPane.showConfirmDialog(contentPane, "共收集本地英文翻译 ${data.items.size} 条，已准备好上传!点击确定即可上传", "上传英文翻译", JOptionPane.YES_NO_CANCEL_OPTION)
                        if (select == JOptionPane.YES_OPTION) {
                            val result = TranslateDataHelper.uploadEnTranslate(data)
                            appendText("本地英文上传数据完成，上传结果为 $result")
                        }
                    }
                } catch (e: Throwable) {
                    appendText(e.message.toString())
                }
                buttonUploadEn?.isEnabled = true
            }.start()
        }

        buttonClear.addActionListener {
            textArea.text = ""
            initText()
            val bar = jsp.verticalScrollBar
            bar.value = bar.maximum + 100
        }

        buttonUploadPre.addActionListener {
            if (TextUtils.isEmpty(currPathString)) {
                appendText("请先选择项目地址！")
                JOptionPane.showMessageDialog(contentPane, "请先选择项目地址", "系统信息",
                        JOptionPane.WARNING_MESSAGE);
                return@addActionListener
            }
            buttonUploadPre.isEnabled = false
            appendText("正在收集数据中...")
            Thread {
                try {
                    val data = if (currentClientType == 0) TranslateFormatHelper.preConvertLocalDataToRemote(currPathString
                            ?: "") else TranslateFormatHelper.preConvertLocalDataToRemoteIOS(currPathString ?: "")
                    val map = mutableMapOf<String, Int>()
                    data.items.forEach {
                        if (map.containsKey(it.key)) {
                            map[it.key] = map[it.key]!! + 1
                        } else {
                            map[it.key] = 1
                        }
                    }
                    val tempMap = map.filter { it.value > 1 }
                    if (!tempMap.isEmpty()) {
                        val result = StringBuilder()
                        tempMap.forEach { t, u ->
                            result.append(t + " 出现了 " + u + " 次\n")
                        }
                        val newResult = result.substring(0, result.length - 2)
                        JOptionPane.showMessageDialog(contentPane, "系统检测到以下KEY中存在多次提交，请检查并删除重复KEY方可上传：\n" + newResult, "提交重复KEY",
                                JOptionPane.WARNING_MESSAGE);
                        appendText("收集完成，系统检测到以下KEY中存在多次提交，请检查并删除重复KEY：\n" + newResult)
                    } else {
                        uploadData = data
                        appendText("共收集本地翻译 ${data.items.size} 条，已准备好上传!")
                    }
                } catch (e: Throwable) {
                    appendText(e.message.toString())
                }
                buttonUploadPre.isEnabled = true
            }.start()
        }

        buttonUpload.addActionListener {
            if (uploadData == null) {
                appendText("请先预加载本地数据！")
                JOptionPane.showMessageDialog(contentPane, "请先预加载本地数据", "系统信息",
                        JOptionPane.WARNING_MESSAGE);
                return@addActionListener
            }
            uploadData?.token = localToken
            buttonUpload.isEnabled = false
            appendText("正在上传本地数据，请耐心等待...")
            Thread {
                val result = TranslateDataHelper.uploadTranslate(uploadData!!)
                uploadData = null
                appendText("本地上传数据完成，上传结果为 $result")
                buttonUpload.isEnabled = true
            }.start()
        }

        buttonDownloadPre.addActionListener {
            appendText("正在下载远程数据，请耐心等待...")
            buttonDownloadPre.isEnabled = false
            Thread {
                val data = if (currentClientType == 0) TranslateDataHelper.downloadTranslate(localToken) else TranslateDataHelper.downloadTranslateIOS(localToken)
                if (data?.code == 200) {
                    appendText("远程数据下载成功，共拉取远程数据" + data.data.count + "条")
                    this.downData = data
                } else {
                    appendText("远程数据下载失败，失败原因为：" + data?.msg)
                }
                buttonDownloadPre.isEnabled = true
            }.start()
        }

        buttonDownload.addActionListener {
            if (TextUtils.isEmpty(currPathString)) {
                appendText("请先选择项目地址！")
                JOptionPane.showMessageDialog(contentPane, "请先选择项目地址", "系统信息",
                        JOptionPane.WARNING_MESSAGE);
                return@addActionListener
            }
            if (downData == null) {
                appendText("请先拉取远程数据！")
                JOptionPane.showMessageDialog(contentPane, "请先拉取远程数据", "系统信息",
                        JOptionPane.WARNING_MESSAGE);
                return@addActionListener
            }
            buttonDownload.isEnabled = false
            appendText("正在将远程数据合并到本地！")
            Thread {
                try {
                    appendText("本地数据已合并完成！")
                    if (currentClientType == 0) {
                        TranslateFormatHelper.convertRemoteDataToLocal(downData, currPathString!!)
                    } else {
                        appendText(TranslateFormatHelper.convertRemoteDataToLocalIOS(downData, currPathString!!))
                    }
                    downData = null
                } catch (e: Throwable) {
                    appendText(e.message.toString())
                }
                buttonDownload.isEnabled = true
            }.start()
        }

        buttonTranslate.addActionListener {
            openBroswer()
        }

        isVisible = true
    }

    private fun initText() {
        textArea.append("""
    欢迎使用【阿念翻译同步助手】 
    1、初次使用工具，需要选择项目根目录，请点击【选择项目】按钮选择需要翻译的项目工程根目录
    2、如果你需要上传翻译，请先点击【预加载本地数据】按钮收集本地数据，再点击【上传本地数据到云端】上传本地数据
    3、上传翻译成功之后，请访问 $adminUrl 查看上传结果
    4、如果你需要同步云端翻译，请先点击【远程下载数据到本地】将数据下载到本地，再点击【合并远程数据到本地】将数据合并到项目中
    5、使用中有什么问题可联系：mxnzp_life@163.com
    6、当前版本号：${Config.VERSION}
    
    """.trimIndent())
        textArea.append("-----------------------------------------\n")
    }

    private var formator = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    private fun appendText(text: String) {
        textArea?.append(formator.format(Date()) + "：" + text + "\n")
        val bar = jsp.verticalScrollBar
        bar.value = bar.maximum + 100
    }

    private fun openBroswer() {
        if (java.awt.Desktop.isDesktopSupported()) {
            try {
                val uri = URI.create(adminUrl)
                val desktop = Desktop.getDesktop()
                if (desktop.isSupported(java.awt.Desktop.Action.BROWSE)) {
                    desktop.browse(uri);
                    appendText("$adminUrl 链接打开成功")
                } else {
                    appendText("$adminUrl 链接打开失败，系统不支持")
                }
            } catch (e: Throwable) {
                appendText("$adminUrl 链接打开失败，系统不支持")
            }
        }
    }
}
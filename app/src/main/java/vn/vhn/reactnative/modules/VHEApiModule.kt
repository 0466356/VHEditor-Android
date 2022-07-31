package vn.vhn.reactnative.modules

import com.facebook.react.bridge.*
import vn.vhn.vhscode.CodeServerService
import vn.vhn.vhscode.root.NewSessionActivity
import java.io.File

interface VHEApiModuleHandler {
    fun onVHEApiStartSession(command: String, name: String?)
    fun onVHEApiOpenEditorPath(folder: String?, paths: List<String>)
}

class VHEApiModule(reactContext: ReactApplicationContext) :
    ReactContextBaseJavaModule(reactContext) {
    override fun getName() = "VHEApi"

    val mDefaultHandler = object : VHEApiModuleHandler {
        override fun onVHEApiStartSession(command: String, name: String?) {
            TODO("Not yet implemented")
        }

        override fun onVHEApiOpenEditorPath(folder: String?, paths: List<String>) {
            TODO("Not yet implemented")
        }

    }

    @ReactMethod(isBlockingSynchronousMethod = true)
    fun startSession(config: ReadableMap) {
        if (!config.hasKey("command")) throw RuntimeException("No command defined")
        var handler: VHEApiModuleHandler = mDefaultHandler
        (currentActivity as? VHEApiModuleHandler)?.apply {
            handler = this
        }
        handler.onVHEApiStartSession(
            config.getString("command")!!,
            config.getString("title")
        )
    }

    @ReactMethod(isBlockingSynchronousMethod = true)
    fun openEditor(config: ReadableMap) {
        if (!config.hasKey("folder") && !config.hasKey("path") && config.getArray("paths")
                .let { it == null || it.size() == 0 }
        ) throw RuntimeException("No path defined")
        var handler: VHEApiModuleHandler = mDefaultHandler
        (currentActivity as? VHEApiModuleHandler)?.apply {
            handler = this
        }
        val paths = mutableListOf<String>()
        config.getString("path")?.also { paths.add(RNFileModule.resolveFile(it)) }
        config.getArray("paths")?.also {
            for (i in 0 until it.size()) {
                paths.add(RNFileModule.resolveFile(it.getString(i)))
            }
        }
        handler.onVHEApiOpenEditorPath(
            config.getString("folder")?.let { RNFileModule.resolveFile(it) },
            paths
        )
    }
}
package com.yc.emotion.home.index.view

import com.yc.emotion.home.base.view.IView
import com.yc.emotion.home.index.domain.bean.SmartChatItem

/**
 *
 * Created by suns  on 2020/8/11 18:32.
 */
interface AIChatView : IView {
    fun showAIContent(data: String?)
    fun showUseCountUp(message: String)//次数用尽了
    fun showSmartAiInfos(data: SmartChatItem?, refresh: Boolean)
}
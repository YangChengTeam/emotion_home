package com.yc.emotion.home.pay.model

import android.content.Context
import com.yc.emotion.home.base.domain.engine.OrderEngine
import com.yc.emotion.home.base.domain.model.IModel

/**
 *
 * Created by suns  on 2019/11/13 16:26.
 */
class VipModel(override var context: Context?) : IModel, OrderEngine(context)
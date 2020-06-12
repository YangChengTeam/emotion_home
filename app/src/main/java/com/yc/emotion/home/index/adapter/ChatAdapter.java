package com.yc.emotion.home.index.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.emotion.home.R;
import com.yc.emotion.home.index.domain.bean.ChatItem;

import java.util.List;

import androidx.annotation.Nullable;

/**
 * Created by suns  on 2020/5/30 11:17.
 */
public class ChatAdapter extends BaseMultiItemQuickAdapter<ChatItem, BaseViewHolder> {
    public ChatAdapter(@Nullable List<ChatItem> data) {
        super(data);
        addItemType(ChatItem.TYPE_OTHER, R.layout.chat_item_other_view);
        addItemType(ChatItem.TYPE_ME, R.layout.chat_item_my_view);
        addItemType(ChatItem.TYPE_NOTIFICATION, R.layout.chat_item_notification);
        addItemType(ChatItem.TYPE_COME_CHAT, R.layout.chat_item_come_in);
        addItemType(ChatItem.TYPE_GET_WX, R.layout.chat_item_get_wx);
    }

    @Override
    protected void convert(BaseViewHolder helper, ChatItem item) {
        switch (item.getItemType()) {
            case ChatItem.TYPE_ME:
            case ChatItem.TYPE_OTHER:
                String userName = item.getUsername();

                helper.setText(R.id.tv_chat_name, replaceStr(userName))
                        .setText(R.id.tv_chat_message, item.getMessage());
                Glide.with(mContext).load(item.getFace()).error(R.drawable.default_avatar_72)
                        .circleCrop().into((ImageView) helper.getView(R.id.iv_avtor));
                break;
            case ChatItem.TYPE_NOTIFICATION:
                helper.setText(R.id.tv_mess_name, "我是" + item.getUsername());
                Glide.with(mContext).load(item.getFace()).error(R.drawable.default_avatar_72)
                        .circleCrop().into((ImageView) helper.getView(R.id.iv_avtor));
                break;
            case ChatItem.TYPE_COME_CHAT:
                helper.setText(R.id.tv_go_chat, replaceStr(item.getUsername()) + "  进入聊天室");
                break;
            case ChatItem.TYPE_GET_WX:
                helper.setText(R.id.tv_nickname, replaceStr(item.getUsername()) + "  获取了");
                break;
        }

    }

    private String replaceStr(String str) {
        int length = str.length();
        if (length == 0 || length == 1) {
            return str;
        }
        String result;
        if (length == 2) {
            result = str.replace(str.substring(1), "*");
        } else {
            int replaceCount = length - 2;
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < replaceCount; i++) {
                sb.append("*");
            }

            result = str.replace(str.substring(1, length - 1), sb.toString());
        }

        return result;
    }
}

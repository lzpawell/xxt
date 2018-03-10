package bid.xiaocha.xxt.adater;

import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Date;
import java.util.List;

import bid.xiaocha.xxt.R;
import bid.xiaocha.xxt.databinding.ListItemShowCommentBinding;
import bid.xiaocha.xxt.databinding.ListItemShowServeCommentBinding;
import bid.xiaocha.xxt.model.CommentEntity;

/**
 * Created by 55039 on 2018/3/4.
 */

public class CommentListAdater extends CommonListAdater<CommentEntity> {
    public CommentListAdater(List<CommentEntity> dataList, LayoutInflater layoutInflater) {
        super(dataList, layoutInflater);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListItemShowCommentBinding binding;
        if (convertView == null){
            binding = DataBindingUtil.inflate(layoutInflater, R.layout.list_item_show_comment,parent,false);
        }else{
            binding = DataBindingUtil.bind(convertView);
        }
        binding.tvCommentContent.setText(dataList.get(position).getCommentContent());
        binding.tvCommentId.setText(dataList.get(position).getCommentorId());
        binding.tvCommentType.setText(dataList.get(position).getType() == CommentEntity.DEMANDER ? "需":"供");
        binding.tvCommentMark.setText(dataList.get(position).getMarks()+"分");
        binding.tvCommentDate.setText(new Date(dataList.get(position).getCommentDate())+"");
        return binding.getRoot();
    }
}

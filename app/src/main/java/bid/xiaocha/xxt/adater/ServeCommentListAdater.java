package bid.xiaocha.xxt.adater;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Date;
import java.util.List;

import bid.xiaocha.xxt.R;
import bid.xiaocha.xxt.databinding.ListItemShowServeCommentBinding;
import bid.xiaocha.xxt.model.ServeCommentEntity;

/**
 * Created by 55039 on 2017/12/30.
 */

public class ServeCommentListAdater extends CommonListAdater<ServeCommentEntity> {
    public ServeCommentListAdater(List<ServeCommentEntity> dataList, LayoutInflater layoutInflater) {
        super(dataList, layoutInflater);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListItemShowServeCommentBinding binding;
        if (convertView == null){
            binding = DataBindingUtil.inflate(layoutInflater, R.layout.list_item_show_serve_comment,parent,false);
        }else{
            binding = DataBindingUtil.bind(convertView);
        }
        binding.tvCommentContent.setText(dataList.get(position).getCommentContent());
        binding.tvCommentId.setText(dataList.get(position).getCommentorId());
        binding.tvCommentMark.setText(dataList.get(position).getMark()+"分");
        binding.tvCommentDate.setText(new Date(dataList.get(position).getCommentDate())+"");
        return binding.getRoot();
    }
}

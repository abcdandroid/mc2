package com.example.mechanic2.adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mechanic2.R;
import com.example.mechanic2.activities.AnswersActivity;
import com.example.mechanic2.app.Application;
import com.example.mechanic2.app.SharedPrefUtils;
import com.example.mechanic2.app.app;
import com.example.mechanic2.models.Question;
import com.example.mechanic2.views.MyTextView;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuestionRecyclerAdapter extends RecyclerView.Adapter<QuestionRecyclerAdapter.QuestionViewHolder> {

    private List<Question> questionList;
    private Activity activity;

    public QuestionRecyclerAdapter(List<Question> questionList, Activity activity) {
        this.questionList = questionList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(Application.getContext()).inflate(R.layout.item_question, parent, false);
        return new QuestionViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
        holder.bindParams(questionList.get(position));
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    class QuestionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView carName;
        TextView questionText;
        TextView answerCount;
        LinearLayout parent;
        SimpleDraweeView previewQuestion;
        private TextView seenCount;
        private MyTextView titleQuestion;
        LinearLayout warningReport;


        QuestionViewHolder(@NonNull View convertView) {
            super(convertView);
            carName = convertView.findViewById(R.id.car_name);
            questionText = convertView.findViewById(R.id.description_question);
            answerCount = convertView.findViewById(R.id.answer_count);
            parent = convertView.findViewById(R.id.parent);
            previewQuestion = convertView.findViewById(R.id.preview_question);
            seenCount = convertView.findViewById(R.id.seen_count);
            titleQuestion = convertView.findViewById(R.id.title_question);

            previewQuestion.setClipToOutline(true);
            parent.setOnClickListener(this);

        }

        void bindParams(Question question) {

            carName.setText(question.getCarName().trim());
            questionText.setText(question.getQ_text().trim());
            titleQuestion.setText(question.getQ_title().trim());
            answerCount.setText(String.valueOf(question.getAnswerCount()).trim());
            seenCount.setText(String.valueOf(question.getSeen_count()).trim());
            if (question.getQ_image_url1().length() > 10) {
                app.fresco(itemView.getContext(), Application.getContext().getString(R.string.drweb) + question.getQ_image_url1().trim(), previewQuestion);
            } else if (question.getQ_image_url2().length() > 10) {
                app.fresco(itemView.getContext(), Application.getContext().getString(R.string.drweb) + question.getQ_image_url2().trim(), previewQuestion);
            } else if (question.getQ_image_url3().length() > 10) {
                app.fresco(itemView.getContext(), Application.getContext().getString(R.string.drweb) + question.getQ_image_url3().trim(), previewQuestion);
            }

        }

        @Override
        public void onClick(View v) {
            Map<String, String> map = new HashMap<>();
            map.put("route", "addToCounterQuestion");
            map.put("q_id", String.valueOf(questionList.get(getAdapterPosition()).getQ_id()));
            map.put("entrance_id", SharedPrefUtils.getStringData("entranceId"));
            Application.getApi().getDataInString(map).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {

                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {

                }
            });


            Intent intent = new Intent(activity, AnswersActivity.class);
            intent.putExtra("question", questionList.get(getAdapterPosition()));
            activity.startActivity(intent);
        }
    }
}

package com.example.mechanic2.adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mechanic2.R;
import com.example.mechanic2.activities.AnswersActivity;
import com.example.mechanic2.app.Application;
import com.example.mechanic2.app.app;
import com.example.mechanic2.models.Question;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        ImageView previewQuestion;
        private TextView seenCount;


        QuestionViewHolder(@NonNull View convertView) {
            super(convertView);
            carName = convertView.findViewById(R.id.car_name);
            questionText = convertView.findViewById(R.id.description_question);
            answerCount = convertView.findViewById(R.id.answer_count);
            parent = convertView.findViewById(R.id.parent);
            previewQuestion = convertView.findViewById(R.id.preview_question);
            seenCount = convertView.findViewById(R.id.seen_count);
            parent.setOnClickListener(this);
        }

        void bindParams(Question question) {

            carName.setText(question.getCarName());
            questionText.setText(question.getQ_text());

            answerCount.setText(String.valueOf(question.getAnswerCount()));
            seenCount.setText(String.valueOf(question.getSeen_count()));
            if (question.getQ_image_url1().length() > 10) {
                app.l("position:" + getAdapterPosition() +"****1"+question.getQ_id()+"****"+ question.getQ_image_url1());
                Glide.with(Application.getContext()).load("http://drkamal3.com/Mechanic/" + question.getQ_image_url1().trim()).into(previewQuestion);
            } else if (question.getQ_image_url2().length() > 10) {
                app.l("position:" + getAdapterPosition() +"****2"+question.getQ_id()+"****"+ question.getQ_image_url2());
                Glide.with(Application.getContext()).load("http://drkamal3.com/Mechanic/" + question.getQ_image_url2().trim()).into(previewQuestion);
            } else if (question.getQ_image_url3().length() > 10) {
                app.l("position:" + getAdapterPosition() +"****3"+question.getQ_id()+"****"+ question.getQ_image_url3());
                Glide.with(Application.getContext()).load("http://drkamal3.com/Mechanic/" + question.getQ_image_url3().trim()).into(previewQuestion);
            }


        }

        @Override
        public void onClick(View v) {
            Map<String, String> map = new HashMap<>();
            map.put("route", "addToCounterQuestion");
            map.put("q_id", String.valueOf(questionList.get(getAdapterPosition()).getQ_id()));
            map.put("entrance_id", String.valueOf(6));
            Application.getApi().getDataInString(map).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    app.l(response.body() + " seen**");
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    app.l(t.getLocalizedMessage());
                }
            });


            Intent intent = new Intent(activity, AnswersActivity.class);
            intent.putExtra("question", questionList.get(getAdapterPosition()));
            activity.startActivity(intent);
        }
    }
}

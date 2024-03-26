package com.example.e_learningapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.e_learningapp.databinding.QuizQuestionsItemBinding;
import com.example.e_learningapp.model.QuizModel;
import java.util.ArrayList;

public class RecyclerQuestionsAdapter extends RecyclerView.Adapter<RecyclerQuestionsAdapter.Holder> {

    private ArrayList<QuizModel> list = new ArrayList<>();

    private OnItemClick onItemClick;

    public void setList(ArrayList<QuizModel> list) {
        this.list = list;
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        QuizQuestionsItemBinding binding = QuizQuestionsItemBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new Holder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        private QuizQuestionsItemBinding binding;
        public Holder(QuizQuestionsItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.answerA.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleAnswerClick(list.get(getLayoutPosition()).getAnswerA());
                }
            });
            binding.answerB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleAnswerClick(list.get(getLayoutPosition()).getAnswerA());
                }
            });
            binding.answerC.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleAnswerClick(list.get(getLayoutPosition()).getAnswerA());
                }
            });
            binding.answerD.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleAnswerClick(list.get(getLayoutPosition()).getAnswerA());
                }
            });
        }

        public void bind(QuizModel quizModel) {
            binding.question.setText(quizModel.getQuestion());
            binding.answerA.setText(quizModel.getAnswerA());
            binding.answerB.setText(quizModel.getAnswerB());
            binding.answerC.setText(quizModel.getAnswerC());
            binding.answerD.setText(quizModel.getAnswerD());
        }

        private void handleAnswerClick(String userChoice) {
            if (onItemClick != null)
                onItemClick.onClick(list.get(getLayoutPosition()), userChoice);
        }
    }
    public interface OnItemClick {
            void onClick (QuizModel quizModel, String userChoice);
    }
}

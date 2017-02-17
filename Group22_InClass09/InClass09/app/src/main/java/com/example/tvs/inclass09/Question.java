package com.example.tvs.inclass09;

import java.util.List;

/**
 * Created by sanju on 12/6/2016.
 */

public class Question {

    /**
     * id : 0
     * text : Who is the first President of the United States of America?
     * image : http://dev.theappsdr.com/apis/trivia_json/photos/georgewashington.png
     * choices : {"choice":["George Washington","Thomas Jefferson","James Monroe","John Adams","Barack Obama","George Bush","Abraham Lincoln","John F. Kennedy"],"answer":"1"}
     */

    private List<QuestionsBean> questions;

    public List<QuestionsBean> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionsBean> questions) {
        this.questions = questions;
    }

    public static class QuestionsBean {
        private String id;
        private String text;
        private String image;
        /**
         * choice : ["George Washington","Thomas Jefferson","James Monroe","John Adams","Barack Obama","George Bush","Abraham Lincoln","John F. Kennedy"]
         * answer : 1
         */

        private ChoicesBean choices;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public ChoicesBean getChoices() {
            return choices;
        }

        public void setChoices(ChoicesBean choices) {
            this.choices = choices;
        }

        public static class ChoicesBean {
            private String answer;
            private List<String> choice;

            public String getAnswer() {
                return answer;
            }

            public void setAnswer(String answer) {
                this.answer = answer;
            }

            public List<String> getChoice() {
                return choice;
            }

            public void setChoice(List<String> choice) {
                this.choice = choice;
            }
        }
    }
}

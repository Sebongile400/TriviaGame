



package com.example.training;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class HelloApplication extends Application {
    private int questionIndex = 0;
    private int score = 0;

    private final String[] questions = {
            "1. _______ was chief Moorosi's fortress?",
            "2. The name of the cave with painting found in Metolong?",
            "3. What is the stick used by Basotho to fight and dance?",
            "4. Which traditional Basotho hat is commonly worn in Lesotho?",
            "5. Water-fall found in the district of Quthing?"
    };

    private final String[][] options = {
            {"Thabana-ntlenyane", "Thabana-limmele", "Thaba-bosiu", "Thaba-Moorosi"},
            {"Himalayas", "Masitise-caves", "Ha-Baroana", "Matelile"},
            {"Lehlaka", "Leqala", "Moseea", "Molamu"},
            {"Fedora", "Bowler", "Sombrero", "Mokorotlo"},
            {"Qhoali", "Senqu", "Majoana-mabeli", "Maletsunyane"}
    };

    private final int[] answers = {3, 2, 3, 3, 2};

    private Image[] images;

    private Label questionLabel;
    private Label feedbackLabel;
    private Label progressLabel;
    private RadioButton[] optionButtons;
    private ToggleGroup toggleGroup;
    private ImageView questionImage;
    private Label scoreLabel;

    private Stage primaryStage;
    private Scene gameScene;
    private Scene endGameScene;
    private Button nextButton; // Declare the next button at the class level

    @Override
    public void start(Stage primaryStage) throws FileNotFoundException {
        this.primaryStage = primaryStage;
        loadImages();
        questionLabel = new Label();
        feedbackLabel = new Label();
        progressLabel = new Label();
        questionImage = new ImageView();
        toggleGroup = new ToggleGroup();
        optionButtons = new RadioButton[4];

        for (int i = 0; i < 4; i++) {
            optionButtons[i] = new RadioButton();
            optionButtons[i].setToggleGroup(toggleGroup);
            optionButtons[i].setWrapText(true); // Set wrap text for radio buttons
        }

        nextButton = new Button("Next");
        nextButton.getStyleClass().add("next"); // Add the "next" class to the button
        nextButton.setAlignment(Pos.CENTER);
        nextButton.setDisable(true); // Initially disable the button

        nextButton.setOnAction(e -> {
            int selectedOption = getSelectedOption();
            checkAnswer(selectedOption);
            displayFeedback(selectedOption);
            questionIndex++;
            if (questionIndex < questions.length) {
                displayQuestion();
            } else {
                endGame();
            }
            nextButton.setDisable(true); // Disable the button again for the next question
        });

        toggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (toggleGroup.getSelectedToggle() != null) {
                nextButton.setDisable(false); // Enable the button when a selection is made
            }
        });

        HBox optionsBox = new HBox(10);
        optionsBox.getChildren().addAll(optionButtons);
        optionsBox.setAlignment(Pos.CENTER); // Center align the radio buttons

        VBox vBox = new VBox(10);
        vBox.setPadding(new Insets(10));
        vBox.setAlignment(Pos.CENTER); // Center align the VBox content
        vBox.getChildren().addAll(questionLabel, questionImage, optionsBox, feedbackLabel, progressLabel);
        vBox.getChildren().add(nextButton);

        scoreLabel = new Label("Score: 0");
        vBox.getChildren().add(scoreLabel);

        gameScene = new Scene(vBox, 800, 600);

        // Load external CSS file
        String cssFile = getClass().getResource("style.css").toExternalForm();
        gameScene.getStylesheets().add(cssFile);

        primaryStage.setTitle("Lesotho Trivia Game");
        primaryStage.setScene(gameScene);
        displayQuestion();
        primaryStage.show();
    }

    private void loadImages() throws FileNotFoundException {
        images = new Image[]{
                new Image(new FileInputStream("src/main/resources/com/example/training/images/Thaba_Moorosi.jpg")),
                new Image(new FileInputStream("src/main/resources/com/example/training/images/HaBaroana(1).png")),
                new Image(new FileInputStream("src/main/resources/com/example/training/images/Molamu.jpg")),
                new Image(new FileInputStream("src/main/resources/com/example/training/images/Mokorotlo(1).jpg")),
                new Image(new FileInputStream("src/main/resources/com/example/training/images/Majoanamabeli.jpg"))
        };

        for (Image image : images) {
            if (image.isError()) {
                System.out.println("Error loading image: " + image.getUrl());
            }
        }
    }

    private void displayQuestion() {
        questionLabel.setText(questions[questionIndex]);

        Image image = images[questionIndex];
        questionImage.setImage(image);
        questionImage.setFitWidth(400);
        questionImage.setFitHeight(350);

        for (int i = 0; i < 4; i++) {
            optionButtons[i].setText(options[questionIndex][i]);
        }

        feedbackLabel.setText("");
        progressLabel.setText("Progress: Question " + (questionIndex + 1) + " of " + questions.length);
    }

    private int getSelectedOption() {
        for (int i = 0; i < 4; i++) {
            if (optionButtons[i].isSelected()) {
                return i;
            }
        }
        return -1;
    }

    private void checkAnswer(int selectedOption) {
        if (selectedOption == answers[questionIndex]) {
            score++;
            scoreLabel.setText("Score: " + score);
        }
    }

    private void displayFeedback(int selectedOption) {
        if (selectedOption == answers[questionIndex]) {
            feedbackLabel.setText("Correct!");
        } else {
            feedbackLabel.setText("Incorrect. Correct answer: " + options[questionIndex][answers[questionIndex]]);
        }
    }

    private void endGame() {
        Label scoreLabel = new Label("Game Over! Your score: " + score + "/" + questions.length);
        Button replayButton = new Button("Replay");
        Button exitButton = new Button("Exit");

        replayButton.setOnAction(e -> {
            questionIndex = 0;
            score = 0;
            feedbackLabel.setText("");
            toggleGroup.selectToggle(null);
            displayQuestion();
            primaryStage.setScene(gameScene);
        });

        exitButton.setOnAction(e -> {
            primaryStage.close();
        });

        VBox endGameVBox = new VBox(10);
        endGameVBox.setPadding(new Insets(10));
        endGameVBox.getChildren().addAll(scoreLabel, replayButton, exitButton);

        endGameScene = new Scene(endGameVBox, 800, 600);

        // Load external CSS file
        String cssFile = getClass().getResource("style.css").toExternalForm();
        endGameScene.getStylesheets().add(cssFile);

        primaryStage.setScene(endGameScene);
    }

    public static void main(String[] args) {
        launch(args);
    }
}

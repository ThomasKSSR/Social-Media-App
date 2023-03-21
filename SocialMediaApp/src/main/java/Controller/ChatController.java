package Controller;

import Business.Network;
import Domain.Msg;
import Domain.User;
import Utilis.Observer;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.List;

public class ChatController implements Observer {


    @FXML
    TextField  msg_text;

    @FXML
    VBox vbox_msg;
    @FXML
    public void initialize(){

    }



    private Network network;

    private User user1;

    private User user2;



    public void setValues(Network network,User user1,User user2){
        this.network=network;
        network.addObserver(this);
        this.user1=user1;
        this.user2=user2;
        initVbox();
    }

    public void initVbox(){
        List<Msg> messages = network.getMsgOf2Users(user1,user2);
        vbox_msg.getChildren().clear();
        for(Msg msg:messages){
            HBox hBox = new HBox();
            if(msg.getSentBy().equals(user1.getName())) {
                hBox.setAlignment(Pos.CENTER_LEFT);

                hBox.setPadding(new Insets(5,5,5,10));
                Text text = new Text(msg.getContent());
                TextFlow textFlow = new TextFlow(text);
                textFlow.setStyle(" -fx-background-color: rgb(233,233,235);"

                        + " -fx-background-radius: 20px;");
                textFlow.setPadding(new Insets(5,10,5,10));

                hBox.getChildren().add(textFlow);
                vbox_msg.getChildren().add(hBox);
            }
            else{
                hBox.setAlignment(Pos.CENTER_RIGHT);
                hBox.setPadding(new Insets(5,5,5,10));
                Text text = new Text(msg.getContent());
                TextFlow textFlow = new TextFlow(text);
                textFlow.setStyle(" -fx-color: rgb(239,242,255);"
                        + " -fx-background-color: rgb(15,125,252);"
                        + " -fx-background-radius: 20px;");
                textFlow.setPadding(new Insets(5,10,5,10));
                text.setFill(Color.color(0.935,0.945,0.996));
                hBox.getChildren().add(textFlow);
                vbox_msg.getChildren().add(hBox);
            }


        }
    }


    @Override
    public void update() {
        initVbox();
    }

    public void sendMsgHandler(){
        String msg_content = msg_text.getText();

        if(msg_content != ""){
            Msg msg = new Msg(user2.getName(),user1.getName(),msg_content);
            network.add_msg(msg);
            msg_text.clear();
        }

    }


}

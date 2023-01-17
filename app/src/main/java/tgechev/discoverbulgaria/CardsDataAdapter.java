package tgechev.discoverbulgaria;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import java.util.ArrayList;

import tgechev.discoverbulgaria.db.models.BaseEntity;
import tgechev.discoverbulgaria.db.models.Fact;
import tgechev.discoverbulgaria.db.models.Poi;


//An adapter which extends the ArrayAdapter class and accepts Card objects
class CardsDataAdapter extends ArrayAdapter<Card> {
    //private variables used for setting the correct position for the arrays from which data is withdrawn
    private int setPosition;
    private int i;
    private byte numberOfCardsInStack;
    private final ArrayList<BaseEntity> cards;

    //constructor
    CardsDataAdapter(Context applicationContext, ArrayList<BaseEntity> cards, int i) {
        super(applicationContext, i);
        this.cards = cards;

    }
    //A "setter" used for assigning the correct position from which getView should start,
    //depending on the restored index in MainActivity
    void setPosition(int position){
        setPosition = position;
        i = position;
    }
    //A "setter" used for determining the total number of cards available
    void setNumberOfCardsInStack(byte numOfCards){
        numberOfCardsInStack = numOfCards;
    }

    //getView() method called automatically whenever a card has to be inflated
    @Override
    public View getView(int position, View contentView, ViewGroup parent){
        //getting the context from the parent ViewGroup
        Context context = parent.getContext();

        //Initializing a new card object to which content is later assigned
        Card card = new Card();
        String title, text, drawableId;

        //Variable whose value is the calculated current position
        int currentPosition;

        //setting the correct currentPosition in relation with the restored index
        if(setPosition != 0){
            currentPosition = position + setPosition;
            if(currentPosition > numberOfCardsInStack - 1) {
                currentPosition = setPosition - i;
                i--;
                if(i == 0)
                    i = setPosition;
            }
        }else{
            currentPosition = position;
        }
        BaseEntity cardData = cards.get(currentPosition);
        if (cardData.getClass() == Fact.class) {
            text = ((Fact) cardData).description;
            title = ((Fact) cardData).title;
            drawableId = ((Fact) cardData).imageId;
        } else {
            text = ((Poi) cardData).description;
            title = ((Poi) cardData).title;
            drawableId = ((Poi) cardData).imageId;
        }
        //supply the layout for a card
        card.text = (TextView) contentView.findViewById(R.id.fact_text);
        card.title = (TextView) contentView.findViewById(R.id.fact_title);
        card.picture = (ImageView) contentView.findViewById(R.id.fact_image);
        card.text.setText(text);
        card.title.setText(title);
        int imgId = context.getResources().getIdentifier(drawableId, "drawable", context.getPackageName());
        card.picture.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), imgId, context.getTheme()));

        return contentView;
    }
}


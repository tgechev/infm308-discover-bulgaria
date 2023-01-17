package tgechev.discoverbulgaria;


import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.wenchao.cardstack.CardStack;

import java.util.ArrayList;
import java.util.Locale;

import tgechev.discoverbulgaria.db.models.BaseEntity;
import tgechev.discoverbulgaria.db.models.Fact;
import tgechev.discoverbulgaria.db.models.Poi;


public class StackActivity extends AppCompatActivity {

        //variable used for setting the number of cards in the deck, this depends on how many facts
        //have been gathered
        byte numberOfCardsInStack = 0;

        //An array of Card objects containing other objects representing different components like ImageView and TextView
        //Every card object from the array is later added in adapter, where it is "filled" with content
        Card[] cards;

        //An object representing the stack of cards
        CardStack mCardStack;

        //A constant used for saving the stack index, which defines the currently displayed card, whenever the activity restarts
        static final String STACK_INDEX = "StackIndex";

        //A variable whose value is always the currently displayed card
        static int mCurrentIndex = 0;

        //An adapter used for assigning content to the cards
        CardsDataAdapter mCardAdapter;

        //String arrays used for storing websites and videos intended to be opened whenever the user swipes a card
        //in a specific direction
        ArrayList<BaseEntity> cardsData = new ArrayList<>();

        private static final String TAG = "StackActivity";


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            Intent data = this.getIntent();
            ArrayList<Fact> facts = data.getParcelableArrayListExtra("facts");
            ArrayList<Poi> poiList = data.getParcelableArrayListExtra("poi");
            if (facts != null) {
                Log.d(TAG, "received facts: " + facts.size());
                numberOfCardsInStack += facts.size();
                cardsData.addAll(facts);
            }
            if (poiList != null) {
                Log.d(TAG, "received poi: " + poiList.size());
                numberOfCardsInStack += poiList.size();
                cardsData.addAll(poiList);
            }

            cards = new Card[numberOfCardsInStack];

            //Check whether the orientation of the device is portrait or landscape and set the corresponding layouts
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                setContentView(R.layout.activity_stack);

                mCardStack = (CardStack) findViewById(R.id.container);

                mCardStack.setContentResource(R.layout.card_layout_portrait);
            }
            else if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                setContentView(R.layout.activity_stack);
                mCardStack = (CardStack) findViewById(R.id.container);
                mCardStack.setContentResource(R.layout.card_layout_landscape);

            }

            //setting a tool bar as an action bar
            Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
            setSupportActionBar(myToolbar);

            //self-defined method for applying custom font to the action bar title
            applyFontForToolbarTitle(this);

            //Initializing the adapter and the string arrays
            mCardAdapter = new CardsDataAdapter(getApplicationContext(), cardsData,0);
            mCardAdapter.setNumberOfCardsInStack(numberOfCardsInStack);
            // sources = getResources().getStringArray(R.array.sources);
            // videoIds = getResources().getStringArray(R.array.videoIds);

            //Check if the saved instance of the activity is not null
            //If this is the case the current index which has been restored from the state is set as the position from which
            //the adapter should start filling the cards with content
            //i.e. If the activity is paused while displaying the fifth card and then restarted, the adapter should start from
            //position 4 which corresponds to the fifth card
            if(savedInstanceState != null)
                mCardAdapter.setPosition(mCurrentIndex);

            //Adding the card objects to the adapter
            for(int i = 0; i < numberOfCardsInStack; i++){
                mCardAdapter.add(cards[i]);
            }
            //Passing the adapter to the card stack object
            mCardStack.setAdapter(mCardAdapter);
        }

        @Override
        protected void onResume() {
            super.onResume();

            //Setting margin for the stack, this makes every card appear slightly below the previous one
            mCardStack.setStackMargin(20);

            //Instantiating a listener for card events
            MyCardEventListener myListener = new MyCardEventListener();

            //Passing the listener to the card stack object
            mCardStack.setListener(myListener);
        }

    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //If the option in the menu is clicked another activity which contains guidelines and appears as a dialog is started
        if (id == R.id.action_instructions) {
            startActivity(new Intent(this, activity_info.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //Saving the current index in the outState bundle
        outState.putInt(STACK_INDEX, mCurrentIndex);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        //Restoring the saved index from the savedInstanceState bundle
        mCurrentIndex = savedInstanceState.getInt(STACK_INDEX, 0);
    }

    @Override
    public void onBackPressed() {
        //When the back button is pressed the activity is paused and the index is reset to 0
        mCurrentIndex = 0;
        super.onBackPressed();
    }

    //Implementation of the listener class
    private class MyCardEventListener implements CardStack.CardEventListener {

        //Method called on every swipe end
        @Override
        public boolean swipeEnd(int direction, float distance) {
            //if "return true" the dismiss animation will be triggered
            //if false, the card will move back to stack
            //distance is finger swipe distance in dp

            //the direction indicate swipe direction
            //there are four directions
            //  0  |  1
            // ----------
            //  2  |  3

            return (distance > 300);
        }

        //Method called on every swipe start
        @Override
        public boolean swipeStart(int direction, float distance) {
            return true;
        }

        @Override
        public boolean swipeContinue(int i, float v, float v1) {
            return true;
        }

        //Method called whenever a card is discarded from stack
        @Override
        public void discarded(int id, int direction) {

            //If the card is swiped UP and LEFT a method which opens a website with more information is called
            if(direction == 0){
                openLink(mCurrentIndex);
            }
            //If the card is swiped UP and RIGHT a method which opens a video about the statement is called
            else if(direction == 1) {
                watchVideo(mCurrentIndex);
            } else if (direction == 2) {
                openMaps(mCurrentIndex);
            }
            //Increment the index
            mCurrentIndex++;

            //If the last card is discarded the index is reset to 0
            if(mCurrentIndex == numberOfCardsInStack) {
                mCurrentIndex = 0;
            }


        }

        //Method called whenever a card is tapped
        @Override
        public void topCardTapped() {
        }
    }

    //A method which opens a website depending on the card being swiped
    private void openLink(int cardIndex){
        String readMore;
        BaseEntity card = cardsData.get(cardIndex);
        if (card.getClass() == Fact.class) {
            readMore = ((Fact) card).readMore;
        } else {
            readMore = ((Poi) card).readMore;
        }
        if (readMore != null && !readMore.equals("")) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(readMore));
            startActivity(browserIntent);
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "No article available", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void openMaps(int cardIndex) {
            BaseEntity card = cardsData.get(cardIndex);
            if (card.getClass() == Fact.class) {
                Toast toast = Toast.makeText(getApplicationContext(), "No location available", Toast.LENGTH_SHORT);
                toast.show();
            } else {
                double longitude = ((Poi) card).longitude;
                double latitude = ((Poi) card).latitude;

                // Creates an Intent that will load a map of San Francisco
                Uri gmmIntentUri = Uri.parse(String.format(Locale.ENGLISH ,"geo:%f,%f?z=18", latitude, longitude));
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
    }

    //A method which starts a video depending on the card being swiped
    //The implementation is the same as in the openLink() method
    private void watchVideo(int cardIndex){
            String videoId;
            BaseEntity card = cardsData.get(cardIndex);
            if (card.getClass() == Fact.class) {
                videoId = ((Fact) card).videoId;
            } else {
                videoId = ((Poi) card).videoId;
            }
            if (videoId != null && !videoId.equals("")) {
                Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + videoId));
                Intent webIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://www.youtube.com/watch?v=" + videoId));
                try {
                    startActivity(appIntent);
                } catch (ActivityNotFoundException ex) {
                    startActivity(webIntent);
                }
            } else {
                Toast toast = Toast.makeText(getApplicationContext(), "No video available", Toast.LENGTH_SHORT);
                toast.show();
            }
    }
    //A method which sets a custom font to the title in the action bar
    public static void applyFontForToolbarTitle(Activity context){
        Toolbar toolbar = (Toolbar) context.findViewById(R.id.my_toolbar);
        for(int i = 0; i < toolbar.getChildCount(); i++){
            View view = toolbar.getChildAt(i);
            if(view instanceof TextView){
                TextView tv = (TextView) view;
                Typeface titleFont = Typeface.
                        createFromAsset(context.getAssets(), "fonts/CyrillicOld.ttf");
                if(tv.getText().equals(toolbar.getTitle())){
                    tv.setTypeface(titleFont);
                    break;
                }
            }
        }
    }
}

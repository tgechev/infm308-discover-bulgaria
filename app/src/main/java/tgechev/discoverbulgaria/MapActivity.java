package tgechev.discoverbulgaria;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.helper.widget.Carousel;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.text.HtmlCompat;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spanned;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.devs.vectorchildfinder.VectorChildFinder;
import com.devs.vectorchildfinder.VectorDrawableCompat;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import tgechev.discoverbulgaria.constants.Constants;
import tgechev.discoverbulgaria.db.AppDatabase;
import tgechev.discoverbulgaria.db.models.Fact;
import tgechev.discoverbulgaria.db.models.Poi;
import tgechev.discoverbulgaria.db.models.Region;
import tgechev.discoverbulgaria.db.models.Type;

public class MapActivity extends AppCompatActivity {

    List<Region> regions;
    Locale bgLocale = new Locale("bg");
    VectorChildFinder vector;
    ImageView highlightMap;
    Set<String> selectedRegions;
    MotionLayout motionLayout;
    int selectColor;
    int highlightColor;
    String currentRegionId;
    Chip factChip, poiChip, historyChip, natureChip;
    Button showButton;
    AppDatabase db;
    private static final String TAG = "MapActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        db = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, Constants.DB_NAME)
                .allowMainThreadQueries().fallbackToDestructiveMigration()
                .createFromAsset(String.format("db/%s.db", Constants.DB_NAME))
                .build();

        regions = db.regionDao().getAll();

        selectedRegions = new HashSet<>();

        highlightMap = findViewById(R.id.map_highlight_view);
        motionLayout = findViewById(R.id.motion_layout);
        vector = new VectorChildFinder(this, R.drawable.map_highlight, highlightMap);
        selectColor = getColor(R.color.region_select);
        highlightColor = getColor(R.color.region_highlight);

        showButton = findViewById(R.id.show_button);
        factChip = findViewById(R.id.fact_chip);
        poiChip = findViewById(R.id.poi_chip);
        historyChip = findViewById(R.id.history_chip);
        natureChip = findViewById(R.id.nature_chip);
        setShowButtonListener(showButton);


        setupCarousel(highlightMap, vector);
    }

    private void setShowButtonListener(Button showButton) {
        showButton.setOnClickListener(btn -> {
            List<Type> selectedTypes = new ArrayList<>();
            List<Fact> selectedFacts = new ArrayList<>();
            List<Poi> selectedPoi = new ArrayList<>();
            if (historyChip.isChecked()) {
                selectedTypes.add(Type.HISTORY);
            }
            if (natureChip.isChecked()) {
                selectedTypes.add(Type.NATURE);
            }
            if (factChip.isChecked()) {
                selectedFacts = db.factDao()
                        .getAllByRegionsAndTypes(selectedRegions.toArray(new String[0]), selectedTypes.toArray(new Type[0]));
            }
            if (poiChip.isChecked()) {
                selectedPoi = db.poiDao()
                        .getAllByRegionsAndTypes(selectedRegions.toArray(new String[0]), selectedTypes.toArray(new Type[0]));
            }

            Intent showCards = new Intent(this, StackActivity.class);
            showCards.putParcelableArrayListExtra("facts", new ArrayList<>(selectedFacts));
            showCards.putParcelableArrayListExtra("poi", new ArrayList<>(selectedPoi));

            startActivity(showCards);
        });
    }

    private void setCardClickListener(CardView view, String regionId) {
        view.setOnTouchListener(new View.OnTouchListener() {
            private float mInitX = 0f;
            private float mInitY = 0f;

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int mTouchSlop = 2;
                switch (motionEvent.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        mInitX = motionEvent.getX();
                        mInitY = motionEvent.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float moveX = Math.abs(motionEvent.getX() - mInitX);
                        float moveY = Math.abs(motionEvent.getY() - mInitY);

                        if (moveX > mTouchSlop || moveY > mTouchSlop) {
                            MotionEvent obtain = MotionEvent.obtain(motionEvent);
                            obtain.setAction(MotionEvent.ACTION_DOWN);
                            motionLayout.dispatchTouchEvent(obtain);
                            motionLayout.onTouchEvent(obtain);
                            return true;
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (Objects.equals(regionId, currentRegionId)) {
                            VectorDrawableCompat.VFullPath regionPath = vector.findPathByName(regionId);
                            int currentFill = regionPath.getFillColor();
                            if (currentFill == 0) {
                                regionPath.setFillColor(selectColor);
                                selectedRegions.add(regionId);
                            } else if (currentFill == selectColor) {
                                regionPath.setFillColor(0);
                                selectedRegions.remove(regionId);
                            }
                            highlightMap.invalidate();
                            view.performClick();
                        }
                        break;
                }
                return true;
            }
        });
    }

    private void setupCarousel(ImageView mapView, VectorChildFinder vectorMap) {
        Carousel carousel = findViewById(R.id.carousel);
        if (carousel == null) {
            return;
        }

        carousel.setAdapter(new Carousel.Adapter() {

            VectorDrawableCompat.VFullPath lastRegionPath;
            String lastRegionId;

            @Override
            public int count() {
                return regions.size();
            }

            @Override
            public void populate(View view, int index) {
                if (view instanceof CardView) {
                    CardView cardView = (CardView) view;
                    ImageView imgView = cardView.findViewWithTag(getString(R.string.carousel_img));
                    String regionId;
                    if (index < 9) {
                        regionId = String.format(bgLocale, "BG-0%d", index + 1);
                    } else {
                        regionId = String.format(bgLocale, "BG-%d", index + 1);
                    }
                    Optional<Region> region = regions.stream().filter(r -> Objects.equals(r.regionId, regionId)).findFirst();
                    if (region.isPresent()) {
                        int imgId = getResources().getIdentifier(region.get().imageId, "drawable", getPackageName());
                        imgView.setImageDrawable(ResourcesCompat.getDrawable(getResources(), imgId, getTheme()));

                        TextView regionName = cardView.findViewWithTag(getString(R.string.carousel_region_name));
                        regionName.setText(region.get().name);
                        TextView regionArea = cardView.findViewWithTag(getString(R.string.carousel_region_area));
                        Spanned areaText;
                        if (Objects.equals(String.valueOf(region.get().area).split("\\.")[1], "0")) {
                            areaText = HtmlCompat.fromHtml(String.format(bgLocale, "%d м<sup><small>2</small></sup>", region.get().area.intValue()), HtmlCompat.FROM_HTML_MODE_COMPACT);
                        } else {
                            areaText = HtmlCompat.fromHtml(String.format(bgLocale, "%.2f м<sup><small>2</small></sup>", region.get().area), HtmlCompat.FROM_HTML_MODE_COMPACT);
                        }
                        regionArea.setText(areaText);
                        setCardClickListener((CardView) view, regionId);
                    }
                }
            }

            @Override
            public void onNewItem(int index) {
                if (index < 9) {
                    currentRegionId = String.format(bgLocale, "BG-0%d", index + 1);
                } else {
                    currentRegionId = String.format(bgLocale, "BG-%d", index + 1);
                }

                VectorDrawableCompat.VFullPath regionPath = vectorMap.findPathByName(currentRegionId);
                int pathStrokeColor = regionPath.getStrokeColor();
                if (pathStrokeColor == 0) {
                    regionPath.setStrokeColor(highlightColor);
                    regionPath.setStrokeWidth(2);
                }

                if (lastRegionPath != null) {
                    lastRegionPath.setStrokeColor(0);
                }

                lastRegionPath = regionPath;
                lastRegionId = currentRegionId;

                mapView.invalidate();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        db.close();
    }
}
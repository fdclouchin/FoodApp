package com.example.foodapp.Helper;
//https://stackoverflow.com/a/45062745/6555714
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public abstract class SwipeHelper2 extends ItemTouchHelper.SimpleCallback {

    private GestureDetector mGestureDetector;
    private RecyclerView mRecyclerView2;
    private Map<Integer, List<UnderlayButton>> mButtonsBuffer;
    private Queue<Integer> mRecoverQueue;

    private List<UnderlayButton> mButtons;
    private int mSwipedPos = -1;
    private float mSwipeThreshold = 0.5f;
    public int mButtonWidth;

    private GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            for (UnderlayButton button : mButtons) {
                if (button.onClick(e.getX(), e.getY()))
                    break;
            }

            return true;
        }
    };

    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent e) {
            if (mSwipedPos < 0) return false;
            Point point = new Point((int) e.getRawX(), (int) e.getRawY());

            RecyclerView.ViewHolder swipedViewHolder = mRecyclerView2.findViewHolderForAdapterPosition(mSwipedPos);
            if (swipedViewHolder == null) return false; //handle crash after delete
            View swipedItem = swipedViewHolder.itemView;
            Rect rect = new Rect();
            swipedItem.getGlobalVisibleRect(rect);

            if (e.getAction() == MotionEvent.ACTION_DOWN ||
                    e.getAction() == MotionEvent.ACTION_UP ||
                    e.getAction() == MotionEvent.ACTION_MOVE) {
                if (rect.top < point.y && rect.bottom > point.y)
                    mGestureDetector.onTouchEvent(e);
                else {
                    mRecoverQueue.add(mSwipedPos);
                    mSwipedPos = -1;
                    recoverSwipedItem();
                }
            }
            return false;
        }
    };

    public SwipeHelper2(Context context, RecyclerView recyclerView, int buttonWidth) {
        super(0, ItemTouchHelper.LEFT);
        this.mRecyclerView2 = recyclerView;
        this.mButtons = new ArrayList<>();
        this.mGestureDetector = new GestureDetector(context, gestureListener);
        this.mButtonWidth = buttonWidth;
        this.mRecyclerView2.setOnTouchListener(onTouchListener);
        mButtonsBuffer = new HashMap<>();

        mRecoverQueue = new LinkedList<Integer>() {
            @Override
            public boolean add(Integer integer) {
                if (contains(integer))
                    return false;
                else
                    return super.add(integer);
            }
        };

        attachSwipe();
    }

    public void attachSwipe() {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(this);
        itemTouchHelper.attachToRecyclerView(mRecyclerView2);
    }


    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        int pos = viewHolder.getAdapterPosition();

        if (mSwipedPos != pos)
            mRecoverQueue.add(mSwipedPos);

        mSwipedPos = pos;

        if (mButtonsBuffer.containsKey(mSwipedPos))
            mButtons = mButtonsBuffer.get(mSwipedPos);
        else
            mButtons.clear();

        mButtonsBuffer.clear();
        mSwipeThreshold = 0.5f * mButtons.size() * mButtonWidth;
        recoverSwipedItem();
    }

    private synchronized void recoverSwipedItem() {
        while (!mRecoverQueue.isEmpty()) {
            int pos = mRecoverQueue.poll();
            if (pos > -1) {
                mRecyclerView2.getAdapter().notifyItemChanged(pos);
            }
        }
    }

    @Override
    public float getSwipeThreshold(RecyclerView.ViewHolder viewHolder) {
        return mSwipeThreshold;
    }

    @Override
    public float getSwipeEscapeVelocity(float defaultValue) {
        return 0.1f * defaultValue;
    }

    @Override
    public float getSwipeVelocityThreshold(float defaultValue) {
        return 5.0f * defaultValue;
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        int pos = viewHolder.getAdapterPosition();
        float translationX = dX;
        View itemView = viewHolder.itemView;

        if (pos < 0) {
            mSwipedPos = pos;
            return;
        }

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            if (dX < 0) {
                List<UnderlayButton> buffer = new ArrayList<>();

                if (!mButtonsBuffer.containsKey(pos)) {
                    instantiateUnderlayButton(viewHolder, buffer);
                    mButtonsBuffer.put(pos, buffer);
                } else {
                    buffer = mButtonsBuffer.get(pos);
                }

                translationX = dX * buffer.size() * mButtonWidth / itemView.getWidth();
                drawButtons(c, itemView, buffer, pos, translationX);
            }
        }

        super.onChildDraw(c, recyclerView, viewHolder, translationX, dY, actionState, isCurrentlyActive);
    }

    private void drawButtons(Canvas c, View itemView, List<UnderlayButton> buffer, int pos, float dX) {
        float right = itemView.getRight();
        float dButtonWidth = (-1) * dX / buffer.size();

        for (UnderlayButton button : buffer) {
            float left = right - dButtonWidth;
            button.onDraw(c, new RectF( left, itemView.getTop(), right, itemView.getBottom()),pos);
            right = left;
        }
    }

    public abstract void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons);

    public class UnderlayButton {
        private final int textSize;
        private final UnderlayButtonClickListener clickListener;
        private final Context context;
        private final Bitmap bitmap;
        private String text;
        /*private int imageResId;*/
        private int color;
        private int pos;
        private RectF clickRegion;
        private Resources resources;

        public UnderlayButton(Context context, int textSize, String text, Bitmap bitmap, int color, UnderlayButtonClickListener clickListener) {
            this.context = context;
            this.textSize = textSize;
            this.text = text;
            /*this.imageResId = imageResId;*/
            this.bitmap = bitmap;
            this.color = color;
            this.clickListener = clickListener;
            resources = context.getResources();
        }

        public boolean onClick(float x, float y) {
            if (clickRegion != null && clickRegion.contains(x, y)) {
                clickListener.onClick(pos);
                return true;
            }

            return false;
        }

        public void onDraw(Canvas c, RectF rect, int pos) {
            Paint p = new Paint();
            // Draw background
            p.setColor(color);
            c.drawRect(rect, p);
            // Draw Text
            p.setColor(Color.WHITE);
            p.setTextSize(textSize);
            p.setAntiAlias(true);
            p.setSubpixelText(true);
            p.setTypeface(ResourcesCompat.getFont(context, R.font.ubuntu_bold));

            //height of text / bitmap
            float spaceHeight = 20;
            float textWidth = p.measureText(text);

            Rect bounds = new Rect();
            p.getTextBounds(text, 0, text.length(), bounds);
            float combinedHeight = bitmap.getHeight() + spaceHeight + bounds.height();

            c.drawBitmap(bitmap, rect.centerX() - (bitmap.getWidth() / 2), rect.centerY() - (combinedHeight / 2), null);
            c.drawText(text, rect.centerX() - (textWidth / 2), rect.centerY() + (combinedHeight / 2), p);

            clickRegion = rect;
            this.pos = pos;
        }
    }



    public interface UnderlayButtonClickListener {
        void onClick(int pos);
    }
}
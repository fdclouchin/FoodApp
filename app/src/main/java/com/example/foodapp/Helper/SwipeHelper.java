package com.example.foodapp.Helper;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodapp.Interfaces.ButtonClickListener;
import com.example.foodapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public abstract class SwipeHelper extends ItemTouchHelper.SimpleCallback {
    private final GestureDetector mGestureDetector;
    private final RecyclerView mRecyclerView;
    private final Map<Integer, List<ItemButton>> mButtonBuffer;
    private final Queue<Integer> mRecoverQueue;

    private List<ItemButton> mButtonList;
    private int mSwipePosition = -1;
    private float mSwipeThreshold = 0.5f;
    private int mButtonWidth;

    private GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            for (ItemButton button : mButtonList) {
                if (button.onCLick(e.getX(), e.getY()))
                    break;
            }

            return true;
        }
    };

    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (mSwipePosition < 0) return false;
            Point point = new Point((int) event.getRawX(), (int) event.getRawY());

            RecyclerView.ViewHolder swipeViewHolder = mRecyclerView.findViewHolderForAdapterPosition(mSwipePosition);
            if (swipeViewHolder == null) return false; //handle crash after delete
            View swipedItem = swipeViewHolder.itemView;
            Rect rect = new Rect();
            swipedItem.getGlobalVisibleRect(rect);

            if (event.getAction() == MotionEvent.ACTION_DOWN ||
                    event.getAction() == MotionEvent.ACTION_UP ||
                    event.getAction() == MotionEvent.ACTION_MOVE) {
                if (rect.top < point.y && rect.bottom > point.y) {
                    mGestureDetector.onTouchEvent(event);
                } else {
                    mRecoverQueue.add(mSwipePosition);
                    mSwipePosition = -1;
                    recoverSwipedItem();
                }
            }
            return false;
        }
    };

    public SwipeHelper(Context context, RecyclerView recyclerView, int buttonWidth) {
        super(0, ItemTouchHelper.LEFT);
        this.mRecyclerView = recyclerView;
        this.mButtonList = new ArrayList<>();
        this.mGestureDetector = new GestureDetector(context, gestureListener);
        this.mRecyclerView.setOnTouchListener(onTouchListener);
        this.mButtonBuffer = new HashMap<>();
        this.mButtonWidth = buttonWidth;

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

    private void attachSwipe() {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(this);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    public class ItemButton {
        private String text;
        private int imageResID;
        private int textSize;
        private int textColor;
        private int position;
        private RectF clickRegion;
        private ButtonClickListener mListener;
        private Context context;
        private Resources resources;

        public ItemButton(Context context, String text, int imageResID, int textSize, int textColor, ButtonClickListener clickListener) {
            this.context = context;
            this.text = text;
            this.imageResID = imageResID;
            this.textSize = textSize;
            this.textColor = textColor;
            this.mListener = clickListener;
            resources = context.getResources();
        }

        public boolean onCLick(float x, float y) {
            if (clickRegion != null && clickRegion.contains(x, y)) {
                mListener.onClick(position);
                return true;
            }
            return false;
        }

        public void onDraw(Canvas canvas, RectF rectF, int pos) {
            Paint paint = new Paint();

            paint.setColor(textColor);
            canvas.drawRect(rectF, paint);

            paint.setColor(Color.WHITE);
            paint.setTextSize(textSize);
            paint.setTypeface(ResourcesCompat.getFont(context, R.font.ubuntu_bold));

            Rect rect = new Rect();
            float cHeight = rectF.height();
            float cWidth = rectF.width();
            paint.setTextAlign(Paint.Align.LEFT);
            paint.getTextBounds(text, 0, text.length(), rect);
            /*float x = cWidth / 2f - rect.width() / 2f - rect.left;
            float y = cHeight / 2f + rect.height() / 2f - rect.bottom;
            canvas.drawText(text, rectF.left + x, rectF.top + y, paint);*/

            float x = 0, y = 0;
            if (imageResID == 0) {
                x = cWidth / 2f - rect.width() / 2f - rect.left;
                y = cHeight / 2f + rect.height() / 2f - rect.bottom;
                canvas.drawText(text, rectF.left + x, rectF.top + y, paint);
            } else {
                Drawable drawable = ContextCompat.getDrawable(context, imageResID);
                Bitmap bitmap = drawableToBitmap(drawable);
                canvas.drawBitmap(bitmap, (rectF.left + rectF.right) / 2, (rectF.top + rectF.bottom) / 2, paint);
            }

            clickRegion = rectF;
            this.position = pos;
        }
    }

    private Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable)
            return ((BitmapDrawable) drawable).getBitmap();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvasDrawable = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvasDrawable.getWidth(), canvasDrawable.getHeight());
        drawable.draw(canvasDrawable);
        return bitmap;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int pos = viewHolder.getAdapterPosition();

        if (mSwipePosition != pos) {
            mRecoverQueue.add(mSwipePosition);
        }

        mSwipePosition = pos;

        if (mButtonBuffer.containsKey(mSwipePosition)) {
            mButtonList = mButtonBuffer.get(mSwipePosition);
        } else {
            mButtonList.clear();
        }

        mButtonBuffer.clear();
        mSwipeThreshold = 0.5f * mButtonList.size() * mButtonWidth;
        recoverSwipedItem();
    }

    private synchronized void recoverSwipedItem() {
        while (!mRecoverQueue.isEmpty()) {
            int pos = mRecoverQueue.poll();
            if (pos > 1) {
                mRecyclerView.getAdapter().notifyItemChanged(pos);
            }
        }
    }

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
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        int pos = viewHolder.getAdapterPosition();
        float translationX = dX;
        View itemView = viewHolder.itemView;

        if (pos < 0) {
            mSwipePosition = pos;
            return;
        }

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            if (dX < 0) {
                List<ItemButton> buffer = new ArrayList<>();

                if (!mButtonBuffer.containsKey(pos)) {
                    instantiateItemButton(viewHolder, buffer);
                    mButtonBuffer.put(pos, buffer);
                } else {
                    buffer = mButtonBuffer.get(pos);
                }

                translationX = dX * buffer.size() * mButtonWidth / itemView.getWidth();
                drawButton(c, itemView, buffer, pos, translationX);
            }
        }

        super.onChildDraw(c, recyclerView, viewHolder, translationX, dY, actionState, isCurrentlyActive);
    }

    public abstract void instantiateItemButton(RecyclerView.ViewHolder viewHolder, List<ItemButton> buffer);

    private void drawButton(Canvas c, View itemView, List<ItemButton> buffer, int pos, float translationX) {
        float right = itemView.getRight();
        float dButtonWidth = -1 * translationX / buffer.size();

        for (ItemButton button : buffer) {
            float left = right - dButtonWidth;
            button.onDraw(c, new RectF(left, itemView.getTop(), right, itemView.getBottom()), pos);
            right = left;
        }
    }

}

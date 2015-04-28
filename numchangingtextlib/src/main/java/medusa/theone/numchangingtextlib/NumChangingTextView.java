package medusa.theone.numchangingtextlib;


import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ValueAnimator;

import java.text.DecimalFormat;

/**
 * Created by xiayong on 2015/4/21.
 */
public class NumChangingTextView extends TextView implements Animator.AnimatorListener {

    private static final int STOPPED = 0;

    private static final int RUNNING = 1;

    private int mPlayingState = STOPPED;

    private float number;

    private float fromNumber;

    private long duration = 1500;

    /**
     * 1.int 2.float
     */
    private int numberType = 2;

    private DecimalFormat fnum;

    private EndListener mEndListener = null;

    final static int[] sizeTable = {9, 99, 999, 9999, 99999, 999999, 9999999,
            99999999, 999999999, Integer.MAX_VALUE};

    public NumChangingTextView(Context context) {
        super(context);
    }

    public NumChangingTextView(Context context, AttributeSet attr) {
        super(context, attr);
    }

    public NumChangingTextView(Context context, AttributeSet attr, int defStyle) {
        super(context, attr, defStyle);
    }


    public interface EndListener {
        public void onEndFinish();
    }


    public boolean isRunning() {
        return (mPlayingState == RUNNING);
    }

    /**
     * @param speedfactor Degree to which the animation should be eased. Seting
     *                    factor to 1.0f produces a y=x^2 parabola. Increasing factor above
     *                    1.0f  exaggerates the ease-in effect (i.e., it starts even
     *                    slower and ends evens faster)
     */
    private void runFloat(float speedfactor) {
        ValueAnimator accelerateAnimator = ValueAnimator.ofFloat(0, number / 2);
        accelerateAnimator.setDuration(duration / 2);
        accelerateAnimator.setInterpolator(new AccelerateInterpolator(speedfactor));
        accelerateAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

                setText(fnum.format(Float.parseFloat(valueAnimator.getAnimatedValue().toString())));
            }

        });
        ValueAnimator decelerateAnimator = ValueAnimator.ofFloat(number / 2, number);
        decelerateAnimator.setDuration(duration / 2);
        decelerateAnimator.setInterpolator(new DecelerateInterpolator(speedfactor));
        decelerateAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

                setText(fnum.format(Float.parseFloat(valueAnimator.getAnimatedValue().toString())));
            }


        });
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(accelerateAnimator).before(decelerateAnimator);
        animatorSet.start();
        animatorSet.addListener(this);
    }

    private void runFloat() {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(fromNumber, number);
        valueAnimator.setDuration(duration);

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

                setText(fnum.format(Float.parseFloat(valueAnimator.getAnimatedValue().toString())));
            }


        });
        valueAnimator.start();
        valueAnimator.addListener(this);
    }

    private void runInt() {
        ValueAnimator valueAnimator = ValueAnimator.ofInt((int) fromNumber, (int) number);
        valueAnimator.setDuration(duration);

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

                setText(valueAnimator.getAnimatedValue().toString());
//                if (valueAnimator.getAnimatedFraction() >= 1) {
//                    mPlayingState = STOPPED;
//                    if (mEndListener != null)
//                        mEndListener.onEndFinish();
//                }
            }
        });
        valueAnimator.start();
        valueAnimator.addListener(this);
    }

    private void runInt(float speedfactor) {
        {
            ValueAnimator accelerateAnimator = ValueAnimator.ofInt(0, (int)number / 2);
            accelerateAnimator.setDuration(duration / 2);
            accelerateAnimator.setInterpolator(new AccelerateInterpolator(speedfactor));
            accelerateAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {

                    setText(valueAnimator.getAnimatedValue().toString());
                }

            });
            ValueAnimator decelerateAnimator = ValueAnimator.ofInt((int)number / 2, (int)number);
            decelerateAnimator.setDuration(duration / 2);
            decelerateAnimator.setInterpolator(new DecelerateInterpolator(speedfactor));
            decelerateAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    setText(valueAnimator.getAnimatedValue().toString());
                }


            });
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.play(accelerateAnimator).before(decelerateAnimator);
            animatorSet.start();
            animatorSet.addListener(this);
        }
    }

    static int sizeOfInt(int x) {
        for (int i = 0; ; i++)
            if (x <= sizeTable[i])
                return i + 1;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        fnum = new DecimalFormat("##0.00");
    }

    /**
     * increase the number
     *
     * @param accelerating true :accelerate in the first half and decelerate in the second half.false :constant speed
     */
    public void startIncrease(boolean accelerating) {

        if (!isRunning()) {
            mPlayingState = RUNNING;
            switch (numberType) {
                case 1:
                    //integer
                    if (accelerating) {
                        runInt(5);
                    } else {
                        runInt();
                    }
                    break;
                case 2:
                    //float
                    if (accelerating) {
                        runFloat();
                    } else {
                        runFloat(5);
                    }

            }
            if (numberType == 1) {
                runInt();
            } else {
                runFloat();
            }
        }
    }

    /**
     * decrease the number
     */
    public void startDecrease(boolean accelerating) {

        if (!isRunning()) {
            mPlayingState = RUNNING;
            if (numberType == 1)
                runInt();
            else
                runFloat();
        }
    }

    public NumChangingTextView withNumber(float number) {

        this.number = number;
        numberType = 2;
        if (number > 1000) {
            fromNumber = number - (float) Math.pow(10, sizeOfInt((int) number) - 2);
        } else {
            fromNumber = number / 2;
        }

        return this;
    }

    public NumChangingTextView withNumber(int number) {
        this.number = number;
        numberType = 1;
        if (number > 1000) {
            fromNumber = number - (float) Math.pow(10, sizeOfInt((int) number) - 2);
        } else {
            fromNumber = number / 2;
        }

        return this;

    }

    public NumChangingTextView setDuration(long duration) {
        this.duration = duration;
        return this;
    }

    public void setOnEnd(EndListener callback) {
        mEndListener = callback;
    }

    @Override
    public void onAnimationStart(Animator animator) {
        //you can implement startIncrease listener here if you want!
    }

    @Override
    public void onAnimationEnd(Animator animator) {
        mPlayingState = STOPPED;
        Toast.makeText(this.getContext(), "end!", Toast.LENGTH_LONG).show();
        if (mEndListener != null)
            mEndListener.onEndFinish();
    }

    @Override
    public void onAnimationCancel(Animator animator) {
        //you can implement cancel listener here if you want!
    }

    @Override
    public void onAnimationRepeat(Animator animator) {
        //you can implement repeat listener here if you want!
    }
}

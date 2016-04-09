package info.teib.newtest;

import java.io.Serializable;

/**
 * Клас, який описує об’єкти типу «визначне місце» (landmark).
 *
 * implements Serializable - один зі способів (не найкращий), як зробити об’єкти цього класу придатними для зберігання у
 * Bundle (напр. при повороті телефона). Кращий спосіб - implements Parcelable, але там складніше
 * (<a href="http://developer.android.com/reference/android/os/Parcelable.html">док по Parcelable</a>)
 *
 * @author Paul Danyliuk
 */
public class Landmark implements Serializable {

    /**
     * Просто порядковий номер місця, за яким його ідентифікувати
     */
    int index;

    /**
     * Назва
     */
    String title;

    /**
     * Опис
     */
    String description;

    /**
     * ID картинки, яку показувати для цього місця; один із R.drawable.{id}
     */
    int pictureId;

    /**
     * Чи це місце було переглянуте
     */
    boolean isViewed;

}

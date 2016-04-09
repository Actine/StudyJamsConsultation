package info.teib.newtest;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Адаптер для RecyclerView, який генерує необхідну кількість леяутів для окремих рядків, обгортає їх у «тримачі»
 * (holder) та прив’язує до них дані.
 *
 * Обов’язково мусить extends RecyclerView.Adapter<НазваКласуВашогоХолдера>
 *
 * @author Paul Danyliuk
 */
public class LandmarksAdapter extends RecyclerView.Adapter<LandmarkHolder> {

    // Контекст і контейнер для прев’ю потрібні нам у холдерах
    private Context context;
    private View container;

    // Дані, які показуватиме цей адаптер
    private Landmark[] landmarks;

    /**
     * Створити новий адаптер для наших визначних місць
     *
     * @param context   Контекст (напр., саме Activity), який потрібен холдерам для витягування картинок за ID
     * @param container Контейнер для «повноекранного перегляду» зображень визначних місць, який холдери відкривають по
     *                  кліку
     * @param landmarks Масив визначних місць, який треба відобразити
     */
    public LandmarksAdapter(Context context, View container, Landmark[] landmarks) {
        this.context = context;
        this.container = container;
        this.landmarks = landmarks;
    }

    /**
     * Викликається щоразу, коли треба зробити холдер. На даному етапі просто створюється холдер із леяутом - ніякі дані
     * до нього ще не прив’язуються. Цей метод буде викликаний стільки разів, скільки рядків відображається на екрані в
     * один час.
     *
     * @param parent   сам RecyclerView, його треба передати інфлейтеру, щоби потім можна було правильно поміряти рядки
     * @param viewType тип рядка. RecyclerView підтримує рисування елементів різних типів (наприклад, заголовки серед
     *                 стрічки новин). У нашому випадку всі рядки однакові, тому ігноруємо
     * @return новий холдер
     */
    @Override
    public LandmarkHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Створюємо новий леяут для окремого рядка: «розгортаємо» його із xml
        final View itemView = LayoutInflater.from(context).inflate(R.layout.item_landmark, parent, false);
        // Створюємо новий холдер, передаючи йому все, що треба
        return new LandmarkHolder(context, itemView, container);
    }

    /**
     * Викликається тоді, коли у холдер треба прив’язати дані (на самому початку, а також коли ви гортаєте список, і
     * холдери виходять за межі екрану зверху і вставляються знизу). У цьому методі потрібно перерисувати леяут у
     * холдері
     *
     * @param holder   Холдер, в який треба прив’язати нові дані
     * @param position Позиція списку (індекс даних, які треба вписати в цей холдер)
     */
    @Override
    public void onBindViewHolder(LandmarkHolder holder, int position) {
        holder.setNewData(landmarks[position]);
    }

    /**
     * RecyclerView смикає цей метод щоб дізнатися, скільки елементів усього у списку
     *
     * @return кількість елементів
     */
    @Override
    public int getItemCount() {
        return landmarks.length;
    }

}

package dynoapps.exchange_rates.alarm;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import dynoapps.exchange_rates.R;
import dynoapps.exchange_rates.SourcesManager;
import dynoapps.exchange_rates.data.CurrencySource;
import dynoapps.exchange_rates.event.AlarmUpdateEvent;
import dynoapps.exchange_rates.ui.widget.recyclerview.UpdatableAdapter;
import dynoapps.exchange_rates.util.RateUtils;

/**
 * Created by erdemmac on 14/12/2016.
 */

class AlarmsAdapter extends UpdatableAdapter<List<Alarm>, AlarmsActivity.AlarmViewHolder> {
    private List<Alarm> alarms;

    AlarmsAdapter(ArrayList<Alarm> alarms) {
        this.alarms = new ArrayList<>();
        for (Alarm al : alarms) {
            this.alarms.add(al);
        }
    }

    public void addData(Alarm alarm) {
        if (this.alarms == null) this.alarms = new ArrayList<>();
        this.alarms.add(alarm);
        notifyDataSetChanged();
    }

    @Override
    public AlarmsActivity.AlarmViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_alarm, parent, false);
        return new AlarmsActivity.AlarmViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final AlarmsActivity.AlarmViewHolder holder, int position) {
        final Alarm alarm = alarms.get(position);
        holder.ivType.setRotation(alarm.is_above ? 90 : 270);
        holder.ivType.setColorFilter(Color.parseColor(!alarm.is_above ? "#f44336" : "#4CAF50"));//todo move to colors.
        holder.tvValue.setText(RateUtils.valToUI(alarm.val, alarm.rate_type));
        holder.ivRateType.setImageResource(RateUtils.getRateIcon(alarm.rate_type));
        CurrencySource source = SourcesManager.getSource(alarm.source_type);
        if (source != null) {
            holder.tvSource.setText(source.getName());
            holder.tvSource.setTextColor(source.getColor());
        } else {
            holder.tvSource.setText("-");
            holder.tvSource.setTextColor(Color.parseColor("#222222"));
        }

        holder.vClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = holder.getAdapterPosition();
                AlarmsAdapter.this.alarms.remove(pos);
                notifyItemRemoved(pos);
                AlarmManager.remove(pos);
            }
        });
        holder.swAlarm.setChecked(alarm.is_enabled);
        holder.swAlarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                AlarmManager.getAlarmsHolder().alarms.get(holder.getAdapterPosition()).is_enabled = b;
                alarm.is_enabled = b;
                EventBus.getDefault().post(new AlarmUpdateEvent(null, true, true));
                AlarmManager.persistAlarms();
            }
        });
    }

    @Override
    public int getItemCount() {
        return alarms != null ? alarms.size() : 0;
    }

    @Override
    public void update(@NonNull List<Alarm> updatedData) {
        alarms = updatedData;
        notifyDataSetChanged();
    }
}
package com.dezenbit.habitos.data;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Double;
import java.lang.Exception;
import java.lang.IllegalArgumentException;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class HabitDao_Impl implements HabitDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Habit> __insertionAdapterOfHabit;

  private final EntityInsertionAdapter<HabitCompletion> __insertionAdapterOfHabitCompletion;

  private final EntityDeletionOrUpdateAdapter<Habit> __deletionAdapterOfHabit;

  private final EntityDeletionOrUpdateAdapter<Habit> __updateAdapterOfHabit;

  private final SharedSQLiteStatement __preparedStmtOfDeleteCompletion;

  private final SharedSQLiteStatement __preparedStmtOfDeleteCompletionsForHabit;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAllHabits;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAllCompletions;

  public HabitDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfHabit = new EntityInsertionAdapter<Habit>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `habits` (`id`,`name`,`emoji`,`colorHex`,`frequencyDays`,`reminderHour`,`reminderMinute`,`reminderEnabled`,`createdAtEpochDay`,`archived`,`type`,`targetValue`,`unit`,`category`,`vacationUntilEpochDay`,`sortOrder`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Habit entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindString(3, entity.getEmoji());
        statement.bindString(4, entity.getColorHex());
        statement.bindString(5, entity.getFrequencyDays());
        statement.bindLong(6, entity.getReminderHour());
        statement.bindLong(7, entity.getReminderMinute());
        final int _tmp = entity.getReminderEnabled() ? 1 : 0;
        statement.bindLong(8, _tmp);
        statement.bindLong(9, entity.getCreatedAtEpochDay());
        final int _tmp_1 = entity.getArchived() ? 1 : 0;
        statement.bindLong(10, _tmp_1);
        statement.bindString(11, __HabitType_enumToString(entity.getType()));
        statement.bindDouble(12, entity.getTargetValue());
        statement.bindString(13, entity.getUnit());
        statement.bindString(14, entity.getCategory());
        statement.bindLong(15, entity.getVacationUntilEpochDay());
        statement.bindLong(16, entity.getSortOrder());
      }
    };
    this.__insertionAdapterOfHabitCompletion = new EntityInsertionAdapter<HabitCompletion>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `habit_completions` (`id`,`habitId`,`epochDay`,`note`,`value`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final HabitCompletion entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getHabitId());
        statement.bindLong(3, entity.getEpochDay());
        if (entity.getNote() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getNote());
        }
        if (entity.getValue() == null) {
          statement.bindNull(5);
        } else {
          statement.bindDouble(5, entity.getValue());
        }
      }
    };
    this.__deletionAdapterOfHabit = new EntityDeletionOrUpdateAdapter<Habit>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `habits` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Habit entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfHabit = new EntityDeletionOrUpdateAdapter<Habit>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `habits` SET `id` = ?,`name` = ?,`emoji` = ?,`colorHex` = ?,`frequencyDays` = ?,`reminderHour` = ?,`reminderMinute` = ?,`reminderEnabled` = ?,`createdAtEpochDay` = ?,`archived` = ?,`type` = ?,`targetValue` = ?,`unit` = ?,`category` = ?,`vacationUntilEpochDay` = ?,`sortOrder` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Habit entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindString(3, entity.getEmoji());
        statement.bindString(4, entity.getColorHex());
        statement.bindString(5, entity.getFrequencyDays());
        statement.bindLong(6, entity.getReminderHour());
        statement.bindLong(7, entity.getReminderMinute());
        final int _tmp = entity.getReminderEnabled() ? 1 : 0;
        statement.bindLong(8, _tmp);
        statement.bindLong(9, entity.getCreatedAtEpochDay());
        final int _tmp_1 = entity.getArchived() ? 1 : 0;
        statement.bindLong(10, _tmp_1);
        statement.bindString(11, __HabitType_enumToString(entity.getType()));
        statement.bindDouble(12, entity.getTargetValue());
        statement.bindString(13, entity.getUnit());
        statement.bindString(14, entity.getCategory());
        statement.bindLong(15, entity.getVacationUntilEpochDay());
        statement.bindLong(16, entity.getSortOrder());
        statement.bindLong(17, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteCompletion = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM habit_completions WHERE habitId = ? AND epochDay = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteCompletionsForHabit = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM habit_completions WHERE habitId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteAllHabits = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM habits";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteAllCompletions = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM habit_completions";
        return _query;
      }
    };
  }

  @Override
  public Object insertHabit(final Habit habit, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfHabit.insertAndReturnId(habit);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object upsertCompletion(final HabitCompletion completion,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfHabitCompletion.insert(completion);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteHabit(final Habit habit, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfHabit.handle(habit);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateHabit(final Habit habit, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfHabit.handle(habit);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteCompletion(final long habitId, final long epochDay,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteCompletion.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, habitId);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, epochDay);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteCompletion.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteCompletionsForHabit(final long habitId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteCompletionsForHabit.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, habitId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteCompletionsForHabit.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteAllHabits(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAllHabits.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteAllHabits.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteAllCompletions(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAllCompletions.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteAllCompletions.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<Habit>> getActiveHabits() {
    final String _sql = "SELECT * FROM habits WHERE archived = 0 ORDER BY sortOrder ASC, createdAtEpochDay DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"habits"}, new Callable<List<Habit>>() {
      @Override
      @NonNull
      public List<Habit> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfEmoji = CursorUtil.getColumnIndexOrThrow(_cursor, "emoji");
          final int _cursorIndexOfColorHex = CursorUtil.getColumnIndexOrThrow(_cursor, "colorHex");
          final int _cursorIndexOfFrequencyDays = CursorUtil.getColumnIndexOrThrow(_cursor, "frequencyDays");
          final int _cursorIndexOfReminderHour = CursorUtil.getColumnIndexOrThrow(_cursor, "reminderHour");
          final int _cursorIndexOfReminderMinute = CursorUtil.getColumnIndexOrThrow(_cursor, "reminderMinute");
          final int _cursorIndexOfReminderEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "reminderEnabled");
          final int _cursorIndexOfCreatedAtEpochDay = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAtEpochDay");
          final int _cursorIndexOfArchived = CursorUtil.getColumnIndexOrThrow(_cursor, "archived");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfTargetValue = CursorUtil.getColumnIndexOrThrow(_cursor, "targetValue");
          final int _cursorIndexOfUnit = CursorUtil.getColumnIndexOrThrow(_cursor, "unit");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfVacationUntilEpochDay = CursorUtil.getColumnIndexOrThrow(_cursor, "vacationUntilEpochDay");
          final int _cursorIndexOfSortOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "sortOrder");
          final List<Habit> _result = new ArrayList<Habit>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Habit _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpEmoji;
            _tmpEmoji = _cursor.getString(_cursorIndexOfEmoji);
            final String _tmpColorHex;
            _tmpColorHex = _cursor.getString(_cursorIndexOfColorHex);
            final String _tmpFrequencyDays;
            _tmpFrequencyDays = _cursor.getString(_cursorIndexOfFrequencyDays);
            final int _tmpReminderHour;
            _tmpReminderHour = _cursor.getInt(_cursorIndexOfReminderHour);
            final int _tmpReminderMinute;
            _tmpReminderMinute = _cursor.getInt(_cursorIndexOfReminderMinute);
            final boolean _tmpReminderEnabled;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfReminderEnabled);
            _tmpReminderEnabled = _tmp != 0;
            final long _tmpCreatedAtEpochDay;
            _tmpCreatedAtEpochDay = _cursor.getLong(_cursorIndexOfCreatedAtEpochDay);
            final boolean _tmpArchived;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfArchived);
            _tmpArchived = _tmp_1 != 0;
            final HabitType _tmpType;
            _tmpType = __HabitType_stringToEnum(_cursor.getString(_cursorIndexOfType));
            final double _tmpTargetValue;
            _tmpTargetValue = _cursor.getDouble(_cursorIndexOfTargetValue);
            final String _tmpUnit;
            _tmpUnit = _cursor.getString(_cursorIndexOfUnit);
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final long _tmpVacationUntilEpochDay;
            _tmpVacationUntilEpochDay = _cursor.getLong(_cursorIndexOfVacationUntilEpochDay);
            final int _tmpSortOrder;
            _tmpSortOrder = _cursor.getInt(_cursorIndexOfSortOrder);
            _item = new Habit(_tmpId,_tmpName,_tmpEmoji,_tmpColorHex,_tmpFrequencyDays,_tmpReminderHour,_tmpReminderMinute,_tmpReminderEnabled,_tmpCreatedAtEpochDay,_tmpArchived,_tmpType,_tmpTargetValue,_tmpUnit,_tmpCategory,_tmpVacationUntilEpochDay,_tmpSortOrder);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<Habit> getHabitById(final long habitId) {
    final String _sql = "SELECT * FROM habits WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, habitId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"habits"}, new Callable<Habit>() {
      @Override
      @Nullable
      public Habit call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfEmoji = CursorUtil.getColumnIndexOrThrow(_cursor, "emoji");
          final int _cursorIndexOfColorHex = CursorUtil.getColumnIndexOrThrow(_cursor, "colorHex");
          final int _cursorIndexOfFrequencyDays = CursorUtil.getColumnIndexOrThrow(_cursor, "frequencyDays");
          final int _cursorIndexOfReminderHour = CursorUtil.getColumnIndexOrThrow(_cursor, "reminderHour");
          final int _cursorIndexOfReminderMinute = CursorUtil.getColumnIndexOrThrow(_cursor, "reminderMinute");
          final int _cursorIndexOfReminderEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "reminderEnabled");
          final int _cursorIndexOfCreatedAtEpochDay = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAtEpochDay");
          final int _cursorIndexOfArchived = CursorUtil.getColumnIndexOrThrow(_cursor, "archived");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfTargetValue = CursorUtil.getColumnIndexOrThrow(_cursor, "targetValue");
          final int _cursorIndexOfUnit = CursorUtil.getColumnIndexOrThrow(_cursor, "unit");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfVacationUntilEpochDay = CursorUtil.getColumnIndexOrThrow(_cursor, "vacationUntilEpochDay");
          final int _cursorIndexOfSortOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "sortOrder");
          final Habit _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpEmoji;
            _tmpEmoji = _cursor.getString(_cursorIndexOfEmoji);
            final String _tmpColorHex;
            _tmpColorHex = _cursor.getString(_cursorIndexOfColorHex);
            final String _tmpFrequencyDays;
            _tmpFrequencyDays = _cursor.getString(_cursorIndexOfFrequencyDays);
            final int _tmpReminderHour;
            _tmpReminderHour = _cursor.getInt(_cursorIndexOfReminderHour);
            final int _tmpReminderMinute;
            _tmpReminderMinute = _cursor.getInt(_cursorIndexOfReminderMinute);
            final boolean _tmpReminderEnabled;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfReminderEnabled);
            _tmpReminderEnabled = _tmp != 0;
            final long _tmpCreatedAtEpochDay;
            _tmpCreatedAtEpochDay = _cursor.getLong(_cursorIndexOfCreatedAtEpochDay);
            final boolean _tmpArchived;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfArchived);
            _tmpArchived = _tmp_1 != 0;
            final HabitType _tmpType;
            _tmpType = __HabitType_stringToEnum(_cursor.getString(_cursorIndexOfType));
            final double _tmpTargetValue;
            _tmpTargetValue = _cursor.getDouble(_cursorIndexOfTargetValue);
            final String _tmpUnit;
            _tmpUnit = _cursor.getString(_cursorIndexOfUnit);
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final long _tmpVacationUntilEpochDay;
            _tmpVacationUntilEpochDay = _cursor.getLong(_cursorIndexOfVacationUntilEpochDay);
            final int _tmpSortOrder;
            _tmpSortOrder = _cursor.getInt(_cursorIndexOfSortOrder);
            _result = new Habit(_tmpId,_tmpName,_tmpEmoji,_tmpColorHex,_tmpFrequencyDays,_tmpReminderHour,_tmpReminderMinute,_tmpReminderEnabled,_tmpCreatedAtEpochDay,_tmpArchived,_tmpType,_tmpTargetValue,_tmpUnit,_tmpCategory,_tmpVacationUntilEpochDay,_tmpSortOrder);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getHabitByIdOnce(final long habitId,
      final Continuation<? super Habit> $completion) {
    final String _sql = "SELECT * FROM habits WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, habitId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Habit>() {
      @Override
      @Nullable
      public Habit call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfEmoji = CursorUtil.getColumnIndexOrThrow(_cursor, "emoji");
          final int _cursorIndexOfColorHex = CursorUtil.getColumnIndexOrThrow(_cursor, "colorHex");
          final int _cursorIndexOfFrequencyDays = CursorUtil.getColumnIndexOrThrow(_cursor, "frequencyDays");
          final int _cursorIndexOfReminderHour = CursorUtil.getColumnIndexOrThrow(_cursor, "reminderHour");
          final int _cursorIndexOfReminderMinute = CursorUtil.getColumnIndexOrThrow(_cursor, "reminderMinute");
          final int _cursorIndexOfReminderEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "reminderEnabled");
          final int _cursorIndexOfCreatedAtEpochDay = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAtEpochDay");
          final int _cursorIndexOfArchived = CursorUtil.getColumnIndexOrThrow(_cursor, "archived");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfTargetValue = CursorUtil.getColumnIndexOrThrow(_cursor, "targetValue");
          final int _cursorIndexOfUnit = CursorUtil.getColumnIndexOrThrow(_cursor, "unit");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfVacationUntilEpochDay = CursorUtil.getColumnIndexOrThrow(_cursor, "vacationUntilEpochDay");
          final int _cursorIndexOfSortOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "sortOrder");
          final Habit _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpEmoji;
            _tmpEmoji = _cursor.getString(_cursorIndexOfEmoji);
            final String _tmpColorHex;
            _tmpColorHex = _cursor.getString(_cursorIndexOfColorHex);
            final String _tmpFrequencyDays;
            _tmpFrequencyDays = _cursor.getString(_cursorIndexOfFrequencyDays);
            final int _tmpReminderHour;
            _tmpReminderHour = _cursor.getInt(_cursorIndexOfReminderHour);
            final int _tmpReminderMinute;
            _tmpReminderMinute = _cursor.getInt(_cursorIndexOfReminderMinute);
            final boolean _tmpReminderEnabled;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfReminderEnabled);
            _tmpReminderEnabled = _tmp != 0;
            final long _tmpCreatedAtEpochDay;
            _tmpCreatedAtEpochDay = _cursor.getLong(_cursorIndexOfCreatedAtEpochDay);
            final boolean _tmpArchived;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfArchived);
            _tmpArchived = _tmp_1 != 0;
            final HabitType _tmpType;
            _tmpType = __HabitType_stringToEnum(_cursor.getString(_cursorIndexOfType));
            final double _tmpTargetValue;
            _tmpTargetValue = _cursor.getDouble(_cursorIndexOfTargetValue);
            final String _tmpUnit;
            _tmpUnit = _cursor.getString(_cursorIndexOfUnit);
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final long _tmpVacationUntilEpochDay;
            _tmpVacationUntilEpochDay = _cursor.getLong(_cursorIndexOfVacationUntilEpochDay);
            final int _tmpSortOrder;
            _tmpSortOrder = _cursor.getInt(_cursorIndexOfSortOrder);
            _result = new Habit(_tmpId,_tmpName,_tmpEmoji,_tmpColorHex,_tmpFrequencyDays,_tmpReminderHour,_tmpReminderMinute,_tmpReminderEnabled,_tmpCreatedAtEpochDay,_tmpArchived,_tmpType,_tmpTargetValue,_tmpUnit,_tmpCategory,_tmpVacationUntilEpochDay,_tmpSortOrder);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getActiveHabitsOnce(final Continuation<? super List<Habit>> $completion) {
    final String _sql = "SELECT * FROM habits WHERE archived = 0 ORDER BY sortOrder ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<Habit>>() {
      @Override
      @NonNull
      public List<Habit> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfEmoji = CursorUtil.getColumnIndexOrThrow(_cursor, "emoji");
          final int _cursorIndexOfColorHex = CursorUtil.getColumnIndexOrThrow(_cursor, "colorHex");
          final int _cursorIndexOfFrequencyDays = CursorUtil.getColumnIndexOrThrow(_cursor, "frequencyDays");
          final int _cursorIndexOfReminderHour = CursorUtil.getColumnIndexOrThrow(_cursor, "reminderHour");
          final int _cursorIndexOfReminderMinute = CursorUtil.getColumnIndexOrThrow(_cursor, "reminderMinute");
          final int _cursorIndexOfReminderEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "reminderEnabled");
          final int _cursorIndexOfCreatedAtEpochDay = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAtEpochDay");
          final int _cursorIndexOfArchived = CursorUtil.getColumnIndexOrThrow(_cursor, "archived");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfTargetValue = CursorUtil.getColumnIndexOrThrow(_cursor, "targetValue");
          final int _cursorIndexOfUnit = CursorUtil.getColumnIndexOrThrow(_cursor, "unit");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfVacationUntilEpochDay = CursorUtil.getColumnIndexOrThrow(_cursor, "vacationUntilEpochDay");
          final int _cursorIndexOfSortOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "sortOrder");
          final List<Habit> _result = new ArrayList<Habit>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Habit _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpEmoji;
            _tmpEmoji = _cursor.getString(_cursorIndexOfEmoji);
            final String _tmpColorHex;
            _tmpColorHex = _cursor.getString(_cursorIndexOfColorHex);
            final String _tmpFrequencyDays;
            _tmpFrequencyDays = _cursor.getString(_cursorIndexOfFrequencyDays);
            final int _tmpReminderHour;
            _tmpReminderHour = _cursor.getInt(_cursorIndexOfReminderHour);
            final int _tmpReminderMinute;
            _tmpReminderMinute = _cursor.getInt(_cursorIndexOfReminderMinute);
            final boolean _tmpReminderEnabled;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfReminderEnabled);
            _tmpReminderEnabled = _tmp != 0;
            final long _tmpCreatedAtEpochDay;
            _tmpCreatedAtEpochDay = _cursor.getLong(_cursorIndexOfCreatedAtEpochDay);
            final boolean _tmpArchived;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfArchived);
            _tmpArchived = _tmp_1 != 0;
            final HabitType _tmpType;
            _tmpType = __HabitType_stringToEnum(_cursor.getString(_cursorIndexOfType));
            final double _tmpTargetValue;
            _tmpTargetValue = _cursor.getDouble(_cursorIndexOfTargetValue);
            final String _tmpUnit;
            _tmpUnit = _cursor.getString(_cursorIndexOfUnit);
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final long _tmpVacationUntilEpochDay;
            _tmpVacationUntilEpochDay = _cursor.getLong(_cursorIndexOfVacationUntilEpochDay);
            final int _tmpSortOrder;
            _tmpSortOrder = _cursor.getInt(_cursorIndexOfSortOrder);
            _item = new Habit(_tmpId,_tmpName,_tmpEmoji,_tmpColorHex,_tmpFrequencyDays,_tmpReminderHour,_tmpReminderMinute,_tmpReminderEnabled,_tmpCreatedAtEpochDay,_tmpArchived,_tmpType,_tmpTargetValue,_tmpUnit,_tmpCategory,_tmpVacationUntilEpochDay,_tmpSortOrder);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getMaxSortOrder(final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COALESCE(MAX(sortOrder), -1) FROM habits";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<HabitCompletion>> getCompletionsForHabit(final long habitId) {
    final String _sql = "SELECT * FROM habit_completions WHERE habitId = ? ORDER BY epochDay DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, habitId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"habit_completions"}, new Callable<List<HabitCompletion>>() {
      @Override
      @NonNull
      public List<HabitCompletion> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfHabitId = CursorUtil.getColumnIndexOrThrow(_cursor, "habitId");
          final int _cursorIndexOfEpochDay = CursorUtil.getColumnIndexOrThrow(_cursor, "epochDay");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final int _cursorIndexOfValue = CursorUtil.getColumnIndexOrThrow(_cursor, "value");
          final List<HabitCompletion> _result = new ArrayList<HabitCompletion>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final HabitCompletion _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpHabitId;
            _tmpHabitId = _cursor.getLong(_cursorIndexOfHabitId);
            final long _tmpEpochDay;
            _tmpEpochDay = _cursor.getLong(_cursorIndexOfEpochDay);
            final String _tmpNote;
            if (_cursor.isNull(_cursorIndexOfNote)) {
              _tmpNote = null;
            } else {
              _tmpNote = _cursor.getString(_cursorIndexOfNote);
            }
            final Double _tmpValue;
            if (_cursor.isNull(_cursorIndexOfValue)) {
              _tmpValue = null;
            } else {
              _tmpValue = _cursor.getDouble(_cursorIndexOfValue);
            }
            _item = new HabitCompletion(_tmpId,_tmpHabitId,_tmpEpochDay,_tmpNote,_tmpValue);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<HabitCompletion>> getAllCompletions() {
    final String _sql = "SELECT * FROM habit_completions";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"habit_completions"}, new Callable<List<HabitCompletion>>() {
      @Override
      @NonNull
      public List<HabitCompletion> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfHabitId = CursorUtil.getColumnIndexOrThrow(_cursor, "habitId");
          final int _cursorIndexOfEpochDay = CursorUtil.getColumnIndexOrThrow(_cursor, "epochDay");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final int _cursorIndexOfValue = CursorUtil.getColumnIndexOrThrow(_cursor, "value");
          final List<HabitCompletion> _result = new ArrayList<HabitCompletion>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final HabitCompletion _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpHabitId;
            _tmpHabitId = _cursor.getLong(_cursorIndexOfHabitId);
            final long _tmpEpochDay;
            _tmpEpochDay = _cursor.getLong(_cursorIndexOfEpochDay);
            final String _tmpNote;
            if (_cursor.isNull(_cursorIndexOfNote)) {
              _tmpNote = null;
            } else {
              _tmpNote = _cursor.getString(_cursorIndexOfNote);
            }
            final Double _tmpValue;
            if (_cursor.isNull(_cursorIndexOfValue)) {
              _tmpValue = null;
            } else {
              _tmpValue = _cursor.getDouble(_cursorIndexOfValue);
            }
            _item = new HabitCompletion(_tmpId,_tmpHabitId,_tmpEpochDay,_tmpNote,_tmpValue);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getCompletionOnce(final long habitId, final long epochDay,
      final Continuation<? super HabitCompletion> $completion) {
    final String _sql = "SELECT * FROM habit_completions WHERE habitId = ? AND epochDay = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, habitId);
    _argIndex = 2;
    _statement.bindLong(_argIndex, epochDay);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<HabitCompletion>() {
      @Override
      @Nullable
      public HabitCompletion call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfHabitId = CursorUtil.getColumnIndexOrThrow(_cursor, "habitId");
          final int _cursorIndexOfEpochDay = CursorUtil.getColumnIndexOrThrow(_cursor, "epochDay");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final int _cursorIndexOfValue = CursorUtil.getColumnIndexOrThrow(_cursor, "value");
          final HabitCompletion _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpHabitId;
            _tmpHabitId = _cursor.getLong(_cursorIndexOfHabitId);
            final long _tmpEpochDay;
            _tmpEpochDay = _cursor.getLong(_cursorIndexOfEpochDay);
            final String _tmpNote;
            if (_cursor.isNull(_cursorIndexOfNote)) {
              _tmpNote = null;
            } else {
              _tmpNote = _cursor.getString(_cursorIndexOfNote);
            }
            final Double _tmpValue;
            if (_cursor.isNull(_cursorIndexOfValue)) {
              _tmpValue = null;
            } else {
              _tmpValue = _cursor.getDouble(_cursorIndexOfValue);
            }
            _result = new HabitCompletion(_tmpId,_tmpHabitId,_tmpEpochDay,_tmpNote,_tmpValue);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getAllHabitsOnce(final Continuation<? super List<Habit>> $completion) {
    final String _sql = "SELECT * FROM habits";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<Habit>>() {
      @Override
      @NonNull
      public List<Habit> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfEmoji = CursorUtil.getColumnIndexOrThrow(_cursor, "emoji");
          final int _cursorIndexOfColorHex = CursorUtil.getColumnIndexOrThrow(_cursor, "colorHex");
          final int _cursorIndexOfFrequencyDays = CursorUtil.getColumnIndexOrThrow(_cursor, "frequencyDays");
          final int _cursorIndexOfReminderHour = CursorUtil.getColumnIndexOrThrow(_cursor, "reminderHour");
          final int _cursorIndexOfReminderMinute = CursorUtil.getColumnIndexOrThrow(_cursor, "reminderMinute");
          final int _cursorIndexOfReminderEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "reminderEnabled");
          final int _cursorIndexOfCreatedAtEpochDay = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAtEpochDay");
          final int _cursorIndexOfArchived = CursorUtil.getColumnIndexOrThrow(_cursor, "archived");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfTargetValue = CursorUtil.getColumnIndexOrThrow(_cursor, "targetValue");
          final int _cursorIndexOfUnit = CursorUtil.getColumnIndexOrThrow(_cursor, "unit");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfVacationUntilEpochDay = CursorUtil.getColumnIndexOrThrow(_cursor, "vacationUntilEpochDay");
          final int _cursorIndexOfSortOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "sortOrder");
          final List<Habit> _result = new ArrayList<Habit>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Habit _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpEmoji;
            _tmpEmoji = _cursor.getString(_cursorIndexOfEmoji);
            final String _tmpColorHex;
            _tmpColorHex = _cursor.getString(_cursorIndexOfColorHex);
            final String _tmpFrequencyDays;
            _tmpFrequencyDays = _cursor.getString(_cursorIndexOfFrequencyDays);
            final int _tmpReminderHour;
            _tmpReminderHour = _cursor.getInt(_cursorIndexOfReminderHour);
            final int _tmpReminderMinute;
            _tmpReminderMinute = _cursor.getInt(_cursorIndexOfReminderMinute);
            final boolean _tmpReminderEnabled;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfReminderEnabled);
            _tmpReminderEnabled = _tmp != 0;
            final long _tmpCreatedAtEpochDay;
            _tmpCreatedAtEpochDay = _cursor.getLong(_cursorIndexOfCreatedAtEpochDay);
            final boolean _tmpArchived;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfArchived);
            _tmpArchived = _tmp_1 != 0;
            final HabitType _tmpType;
            _tmpType = __HabitType_stringToEnum(_cursor.getString(_cursorIndexOfType));
            final double _tmpTargetValue;
            _tmpTargetValue = _cursor.getDouble(_cursorIndexOfTargetValue);
            final String _tmpUnit;
            _tmpUnit = _cursor.getString(_cursorIndexOfUnit);
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final long _tmpVacationUntilEpochDay;
            _tmpVacationUntilEpochDay = _cursor.getLong(_cursorIndexOfVacationUntilEpochDay);
            final int _tmpSortOrder;
            _tmpSortOrder = _cursor.getInt(_cursorIndexOfSortOrder);
            _item = new Habit(_tmpId,_tmpName,_tmpEmoji,_tmpColorHex,_tmpFrequencyDays,_tmpReminderHour,_tmpReminderMinute,_tmpReminderEnabled,_tmpCreatedAtEpochDay,_tmpArchived,_tmpType,_tmpTargetValue,_tmpUnit,_tmpCategory,_tmpVacationUntilEpochDay,_tmpSortOrder);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getAllCompletionsOnce(
      final Continuation<? super List<HabitCompletion>> $completion) {
    final String _sql = "SELECT * FROM habit_completions";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<HabitCompletion>>() {
      @Override
      @NonNull
      public List<HabitCompletion> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfHabitId = CursorUtil.getColumnIndexOrThrow(_cursor, "habitId");
          final int _cursorIndexOfEpochDay = CursorUtil.getColumnIndexOrThrow(_cursor, "epochDay");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final int _cursorIndexOfValue = CursorUtil.getColumnIndexOrThrow(_cursor, "value");
          final List<HabitCompletion> _result = new ArrayList<HabitCompletion>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final HabitCompletion _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpHabitId;
            _tmpHabitId = _cursor.getLong(_cursorIndexOfHabitId);
            final long _tmpEpochDay;
            _tmpEpochDay = _cursor.getLong(_cursorIndexOfEpochDay);
            final String _tmpNote;
            if (_cursor.isNull(_cursorIndexOfNote)) {
              _tmpNote = null;
            } else {
              _tmpNote = _cursor.getString(_cursorIndexOfNote);
            }
            final Double _tmpValue;
            if (_cursor.isNull(_cursorIndexOfValue)) {
              _tmpValue = null;
            } else {
              _tmpValue = _cursor.getDouble(_cursorIndexOfValue);
            }
            _item = new HabitCompletion(_tmpId,_tmpHabitId,_tmpEpochDay,_tmpNote,_tmpValue);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }

  private String __HabitType_enumToString(@NonNull final HabitType _value) {
    switch (_value) {
      case BOOLEAN: return "BOOLEAN";
      case NUMERIC: return "NUMERIC";
      default: throw new IllegalArgumentException("Can't convert enum to string, unknown enum value: " + _value);
    }
  }

  private HabitType __HabitType_stringToEnum(@NonNull final String _value) {
    switch (_value) {
      case "BOOLEAN": return HabitType.BOOLEAN;
      case "NUMERIC": return HabitType.NUMERIC;
      default: throw new IllegalArgumentException("Can't convert value to enum, unknown value: " + _value);
    }
  }
}

package com.keeppieces.android.logic.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update

@Dao
interface MemberDao {
    @Insert
    fun insertMember(member: Member): Long

    @Update
    fun updateMember(newMember: Member)

    @Delete
    fun deleteMember(member: Member)
}
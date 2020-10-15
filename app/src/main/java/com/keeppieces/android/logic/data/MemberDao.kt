package com.keeppieces.android.logic.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface MemberDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertMember(member: Member)

    @Update
    fun updateMember(newMember: Member)

    @Delete
    fun deleteMember(member: Member)

    @Query("SELECT * FROM member")
    fun getMember(): LiveData<List<Member>>
}
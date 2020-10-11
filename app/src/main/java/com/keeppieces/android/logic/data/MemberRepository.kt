package com.keeppieces.android.logic.data

import androidx.lifecycle.LiveData
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.keeppieces.android.KeepPiecesApplication


class MemberRepository {
    private val context = KeepPiecesApplication.context
    private val memberDao = AppDatabase.getDatabase(context).memberDao()

    fun createMember(member: Member) {
        memberDao.insertMember(member)
    }

    fun getMember() = memberDao.getMember()

    fun updateMember(member: Member) {
        memberDao.updateMember(member)
    }

    fun deleteMember(member: Member) {
        memberDao.deleteMember(member)
    }
}
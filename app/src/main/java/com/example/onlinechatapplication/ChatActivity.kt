package com.example.onlinechatapplication

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChatActivity : AppCompatActivity() {
    private lateinit var messageRecyclerView: RecyclerView
    private lateinit var messageBox: EditText
    private lateinit var sentButton: ImageView
    private lateinit var messageList: ArrayList<Message>
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var databaseReference: DatabaseReference

    var receiverRoom: String?= null
    var senderRoom: String?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_chat)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        databaseReference = FirebaseDatabase.getInstance().getReference()
        val name = intent.getStringExtra("name")
        val receiverUid = intent.getStringExtra("uid")

        val senderUid = FirebaseAuth.getInstance().currentUser?.uid
        senderRoom = receiverUid + senderUid

        receiverRoom = senderUid + receiverUid

        supportActionBar?.title = name
        messageRecyclerView = findViewById(R.id.recycler_view2)
        messageBox = findViewById(R.id.write_message)
        sentButton = findViewById(R.id.sent_message)
        messageList = ArrayList()
        messageAdapter = MessageAdapter(this,messageList)

        messageRecyclerView.layoutManager = LinearLayoutManager(this)
        messageRecyclerView.adapter = messageAdapter

        // logic for adding data to recyclerView
        databaseReference.child("chats").child(senderRoom!!).child("message")
            .addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageList.clear()
                    for(postSnapshot in snapshot.children) {
                        val message = postSnapshot.getValue(Message::class.java)
                        messageList.add(message!!)
                    }
                    messageAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })

        sentButton.setOnClickListener {
            val message = messageBox.text.toString()
            val messageObject = Message(message,senderUid)

            databaseReference.child("chats").child(senderRoom!!).child("message").push()
                .setValue(messageObject).addOnSuccessListener {
                    databaseReference.child("chats").child(receiverRoom!!).child("message").push()
                        .setValue(messageObject)
                }
            messageBox.setText("")
        }
    }
}
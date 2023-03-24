


public class MainActivity extends AppCompatActivity implements NoteAdapter.OnNoteClickListener, FontSizeDialog.FontSizeListener, FontColorDialog.FontColorListener {

    private static final String TAG = "MainActivity";
    private static final int REQUEST_CODE_ADD_NOTE = 1;
    private static final int REQUEST_CODE_EDIT_NOTE = 2;
    private RecyclerView recyclerViewNotes;
    private ArrayList<Note> notes;
    private NoteAdapter noteAdapter;
    private int recentlyDeletedNotePosition;
    private Note recentlyDeletedNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerViewNotes = findViewById(R.id.recyclerViewNotes);
        recyclerViewNotes.setLayoutManager(new LinearLayoutManager(this));

        notes = new ArrayList<>();
        noteAdapter = new NoteAdapter(notes, this);
        recyclerViewNotes.setAdapter(noteAdapter);

        loadNotes();
    }

    private void loadNotes() {
        SQLiteDatabase db = new NotesDBHelper(this).getReadableDatabase();

        Cursor cursor = db.query(
                NotesContract.NoteEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                NotesContract.NoteEntry.COLUMN_TIMESTAMP + " DESC"
        );

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(NotesContract.NoteEntry._ID));
            String title = cursor.getString(cursor.getColumnIndex(NotesContract.NoteEntry.COLUMN_TITLE));
            String content = cursor.getString(cursor.getColumnIndex(NotesContract.NoteEntry.COLUMN_CONTENT));
            String timestamp = cursor.getString(cursor.getColumnIndex(NotesContract.NoteEntry.COLUMN_TIMESTAMP));
            int color = cursor.getInt(cursor.getColumnIndex(NotesContract.NoteEntry.COLUMN_COLOR));
            float textSize = cursor.getFloat(cursor.getColumnIndex(NotesContract.NoteEntry.COLUMN_TEXT_SIZE));
            Note note = new Note(id, title, content, timestamp, color, textSize);
            notes.add(note);
        }

        noteAdapter.notifyDataSetChanged();
        cursor.close();
    }

    private void deleteNote() {
        recentlyDeletedNote = notes.get(recentlyDeletedNotePosition);
        notes.remove(recentlyDeletedNotePosition);
        noteAdapter.notifyItemRemoved(recentlyDeletedNotePosition);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Note deleted")
                .setCancelable(false)
                .setPositiveButton("Undo", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        notes.add(recentlyDeletedNotePosition, recentlyDeletedNote);
                        noteAdapter.notifyItemInserted(recentlyDeletedNotePosition);
                    }
                })
                .setNegativeButton("Delete forever", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SQLiteDatabase db = new NotesDBHelper(MainActivity.this).getWritableDatabase();
                        String selection = NotesContract.NoteEntry._ID + " LIKE ?";
                        String[] selectionArgs = {String.valueOf(recentlyDeletedNote.getId())};
                        db.delete(NotesContract.NoteEntry.TABLE_NAME, selection, selectionArgs);
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void addNote() {
        Intent intent = new Intent(this, NoteEditorActivity.class);
        startActivityForResult(intent, REQUEST_CODE_ADD_NOTE);
    }

    private void editNote() {
        Note note = notes.get(recentlyDeletedNotePosition);
        Intent intent = new Intent(this, NoteEditorActivity.class);
        intent.putExtra(NoteEditorActivity.EXTRA_NOTE_ID, note.getId());
        intent.putExtra(NoteEditorActivity.EXTRA_NOTE_TITLE, note.getTitle());
        intent.putExtra(NoteEditorActivity.EXTRA_NOTE_CONTENT, note.getContent());
        startActivityForResult(intent, REQUEST_CODE_EDIT_NOTE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_ADD_NOTE:
                    loadNotes();
                    break;
                case REQUEST_CODE_EDIT_NOTE:
                    loadNotes();
                    break;
            }
        }
    }

    @Override
    public void onNoteClick(int position) {
        recentlyDeletedNotePosition = position;
        PopupMenu popupMenu = new PopupMenu(this, recyclerViewNotes.getChildAt(position));
        popupMenu.inflate(R.menu.menu_note);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menuNoteEdit:
                        editNote();
                        return true;
                    case R.id.menuNoteDelete:
                        deleteNote();
                        return true;
                    case R.id.menuNoteShare:
                        shareNote();
                        return true;
                    case R.id.menuNoteChangeColor:
                        showFontColorDialog();
                        return true;
                    default:
                        return false;
                }
            }
        });
        popupMenu.show();
    }

    private void shareNote() {
        Note note = notes.get(recentlyDeletedNotePosition);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, note.getTitle());
        intent.putExtra(Intent.EXTRA_TEXT, note.getContent());
        startActivity(Intent.createChooser(intent, "Share note"));
    }

    private void showFontSizeDialog() {
        FontSizeDialog fontSizeDialog = new FontSizeDialog(this);
        fontSizeDialog.show(getSupportFragmentManager(), "font size dialog");
    }

    private void showFontColorDialog() {
        FontColorDialog fontColorDialog = new FontColorDialog(this);
        fontColorDialog.show(getSupportFragmentManager(), "font color dialog");
    }

    @Override
    public void onFontSizeSet(int size) {
        NoteEditorFragment noteEditor = (NoteEditorFragment) getSupportFragmentManager().findFragmentById(R.id.noteEditorFragment);
        noteEditor.setTextSize(size);
        noteAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFontColorSet(int color) {
        NoteEditorFragment noteEditor = (NoteEditorFragment) getSupportFragmentManager().findFragmentById(R.id.noteEditorFragment);
        noteEditor.setFontColor(color);
        noteAdapter.notifyDataSetChanged();
    }
 @Override
    public void onFontSizeSet(int size) {
        //  SharedPreferences
        SharedPreferences.Editor editor = getSharedPreferences("settings", MODE_PRIVATE).edit();
        editor.putInt("font_size", size);
        editor.apply();

        noteEditor.setTextSize(size);
        noteAdapter.notifyDataSetChanged();
    }
}
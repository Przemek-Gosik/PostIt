import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Note } from '../../../models/Note';
import { MatDialog } from '@angular/material/dialog';
import { DeleteNoteDialogComponent } from './delete-note-dialog/delete-note-dialog.component';
import { NoteService } from '../../../services/NoteService';

@Component({
  selector: 'app-post-it-note',
  templateUrl: './post-it-note.component.html',
  styleUrls: ['./post-it-note.component.css'],
})
export class PostItNoteComponent implements OnInit {
  text: string = '';
  editable: boolean = false;

  constructor(private dialog: MatDialog, private noteService: NoteService) {}

  @Input() note?: Note;
  @Output() noteToDelete = new EventEmitter();

  ngOnInit(): void {
    if (this.note) {
      this.text = this.note.text;
    }
  }

  editNote(): void {
    if (this.note) {
      if (this.note.id) {
        this.noteService.getNoteById(this.note.id).subscribe((res: Note) => {
          this.note = res;
        });
        this.text = this.note.text;
      }
      this.editable = true;
    }
  }

  saveNote(): void {
    this.editable = false;
    if (this.note && this.text.length != 0) {
      this.note.text = this.text;
      if (this.note.id) {
        this.noteService.editNote(this.note).subscribe((res: Note) => {
          this.note = res;
        });
      } else {
        this.noteService.createNote(this.note).subscribe((res: any) => {
          this.note = res;
        });
      }
      this.text = this.note.text;
    }
  }

  openDialog(): void {
    let dialogRef = this.dialog.open(DeleteNoteDialogComponent);
    dialogRef.afterClosed().subscribe((res: boolean) => {
      if (res) {
        this.noteToDelete.emit();
      }
    });
  }
}

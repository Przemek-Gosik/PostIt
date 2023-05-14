import { Component, Input, OnInit } from '@angular/core';
import { Note } from '../../../models/Note';
import { MatDialog } from '@angular/material/dialog';
import { DeleteNoteDialogComponent } from './delete-note-dialog/delete-note-dialog.component';

@Component({
  selector: 'app-post-it-note',
  templateUrl: './post-it-note.component.html',
  styleUrls: ['./post-it-note.component.css'],
})
export class PostItNoteComponent implements OnInit {
  text: string = '';
  editable: boolean = false;

  constructor(private dialog: MatDialog) {}

  @Input() note?: Note;

  ngOnInit(): void {
    if (this.note) {
      this.text = this.note.text;
    }
  }

  editNote(): void {
    this.editable = !this.editable;
  }

  openDialog(): void {
    let dialogRef = this.dialog.open(DeleteNoteDialogComponent);
    dialogRef.afterClosed().subscribe((res) => {
      console.log(res);
    });
  }
}

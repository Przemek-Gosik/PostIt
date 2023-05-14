import { Component, Input, OnInit } from '@angular/core';
import { Note } from '../../../models/Note';

@Component({
  selector: 'app-post-it-note',
  templateUrl: './post-it-note.component.html',
  styleUrls: ['./post-it-note.component.css'],
})
export class PostItNoteComponent implements OnInit {
  text: string = '';
  editable: boolean = false;

  constructor() {}

  @Input() note?: Note;

  ngOnInit(): void {
    if (this.note) {
      this.text = this.note.text;
    }
  }

  editNote(): void {
    this.editable = !this.editable;
  }
}

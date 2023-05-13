import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PostItNoteComponent } from './post-it-note.component';

describe('PostItNoteComponent', () => {
  let component: PostItNoteComponent;
  let fixture: ComponentFixture<PostItNoteComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PostItNoteComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PostItNoteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

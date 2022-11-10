import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ExpenseTypeDetailComponent } from './expense-type-detail.component';

describe('ExpenseType Management Detail Component', () => {
  let comp: ExpenseTypeDetailComponent;
  let fixture: ComponentFixture<ExpenseTypeDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ExpenseTypeDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ expenseType: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ExpenseTypeDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ExpenseTypeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load expenseType on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.expenseType).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});

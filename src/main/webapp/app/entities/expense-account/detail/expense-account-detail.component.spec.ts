import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ExpenseAccountDetailComponent } from './expense-account-detail.component';

describe('ExpenseAccount Management Detail Component', () => {
  let comp: ExpenseAccountDetailComponent;
  let fixture: ComponentFixture<ExpenseAccountDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ExpenseAccountDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ expenseAccount: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ExpenseAccountDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ExpenseAccountDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load expenseAccount on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.expenseAccount).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});

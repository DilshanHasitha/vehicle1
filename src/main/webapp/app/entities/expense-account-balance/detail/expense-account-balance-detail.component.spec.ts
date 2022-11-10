import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ExpenseAccountBalanceDetailComponent } from './expense-account-balance-detail.component';

describe('ExpenseAccountBalance Management Detail Component', () => {
  let comp: ExpenseAccountBalanceDetailComponent;
  let fixture: ComponentFixture<ExpenseAccountBalanceDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ExpenseAccountBalanceDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ expenseAccountBalance: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ExpenseAccountBalanceDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ExpenseAccountBalanceDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load expenseAccountBalance on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.expenseAccountBalance).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CashBookBalanceDetailComponent } from './cash-book-balance-detail.component';

describe('CashBookBalance Management Detail Component', () => {
  let comp: CashBookBalanceDetailComponent;
  let fixture: ComponentFixture<CashBookBalanceDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CashBookBalanceDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ cashBookBalance: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CashBookBalanceDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CashBookBalanceDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load cashBookBalance on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.cashBookBalance).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});

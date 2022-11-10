import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TransactionTypeDetailComponent } from './transaction-type-detail.component';

describe('TransactionType Management Detail Component', () => {
  let comp: TransactionTypeDetailComponent;
  let fixture: ComponentFixture<TransactionTypeDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TransactionTypeDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ transactionType: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(TransactionTypeDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(TransactionTypeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load transactionType on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.transactionType).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});

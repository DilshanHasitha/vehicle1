import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CashBookDetailComponent } from './cash-book-detail.component';

describe('CashBook Management Detail Component', () => {
  let comp: CashBookDetailComponent;
  let fixture: ComponentFixture<CashBookDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CashBookDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ cashBook: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CashBookDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CashBookDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load cashBook on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.cashBook).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});

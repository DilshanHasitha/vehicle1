import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MerchantDetailComponent } from './merchant-detail.component';

describe('Merchant Management Detail Component', () => {
  let comp: MerchantDetailComponent;
  let fixture: ComponentFixture<MerchantDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MerchantDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ merchant: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(MerchantDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(MerchantDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load merchant on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.merchant).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});

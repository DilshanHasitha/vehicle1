import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BannersDetailComponent } from './banners-detail.component';

describe('Banners Management Detail Component', () => {
  let comp: BannersDetailComponent;
  let fixture: ComponentFixture<BannersDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [BannersDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ banners: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(BannersDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(BannersDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load banners on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.banners).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { BannersFormService } from './banners-form.service';
import { BannersService } from '../service/banners.service';
import { IBanners } from '../banners.model';
import { IImages } from 'app/entities/images/images.model';
import { ImagesService } from 'app/entities/images/service/images.service';

import { BannersUpdateComponent } from './banners-update.component';

describe('Banners Management Update Component', () => {
  let comp: BannersUpdateComponent;
  let fixture: ComponentFixture<BannersUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let bannersFormService: BannersFormService;
  let bannersService: BannersService;
  let imagesService: ImagesService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [BannersUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(BannersUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BannersUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    bannersFormService = TestBed.inject(BannersFormService);
    bannersService = TestBed.inject(BannersService);
    imagesService = TestBed.inject(ImagesService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Images query and add missing value', () => {
      const banners: IBanners = { id: 456 };
      const images: IImages[] = [{ id: 49381 }];
      banners.images = images;

      const imagesCollection: IImages[] = [{ id: 66942 }];
      jest.spyOn(imagesService, 'query').mockReturnValue(of(new HttpResponse({ body: imagesCollection })));
      const additionalImages = [...images];
      const expectedCollection: IImages[] = [...additionalImages, ...imagesCollection];
      jest.spyOn(imagesService, 'addImagesToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ banners });
      comp.ngOnInit();

      expect(imagesService.query).toHaveBeenCalled();
      expect(imagesService.addImagesToCollectionIfMissing).toHaveBeenCalledWith(
        imagesCollection,
        ...additionalImages.map(expect.objectContaining)
      );
      expect(comp.imagesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const banners: IBanners = { id: 456 };
      const images: IImages = { id: 14907 };
      banners.images = [images];

      activatedRoute.data = of({ banners });
      comp.ngOnInit();

      expect(comp.imagesSharedCollection).toContain(images);
      expect(comp.banners).toEqual(banners);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBanners>>();
      const banners = { id: 123 };
      jest.spyOn(bannersFormService, 'getBanners').mockReturnValue(banners);
      jest.spyOn(bannersService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ banners });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: banners }));
      saveSubject.complete();

      // THEN
      expect(bannersFormService.getBanners).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(bannersService.update).toHaveBeenCalledWith(expect.objectContaining(banners));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBanners>>();
      const banners = { id: 123 };
      jest.spyOn(bannersFormService, 'getBanners').mockReturnValue({ id: null });
      jest.spyOn(bannersService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ banners: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: banners }));
      saveSubject.complete();

      // THEN
      expect(bannersFormService.getBanners).toHaveBeenCalled();
      expect(bannersService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBanners>>();
      const banners = { id: 123 };
      jest.spyOn(bannersService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ banners });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(bannersService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareImages', () => {
      it('Should forward to imagesService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(imagesService, 'compareImages');
        comp.compareImages(entity, entity2);
        expect(imagesService.compareImages).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});

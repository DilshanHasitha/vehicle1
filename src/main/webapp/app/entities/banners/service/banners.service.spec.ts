import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IBanners } from '../banners.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../banners.test-samples';

import { BannersService } from './banners.service';

const requireRestSample: IBanners = {
  ...sampleWithRequiredData,
};

describe('Banners Service', () => {
  let service: BannersService;
  let httpMock: HttpTestingController;
  let expectedResult: IBanners | IBanners[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(BannersService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Banners', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const banners = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(banners).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Banners', () => {
      const banners = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(banners).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Banners', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Banners', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Banners', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addBannersToCollectionIfMissing', () => {
      it('should add a Banners to an empty array', () => {
        const banners: IBanners = sampleWithRequiredData;
        expectedResult = service.addBannersToCollectionIfMissing([], banners);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(banners);
      });

      it('should not add a Banners to an array that contains it', () => {
        const banners: IBanners = sampleWithRequiredData;
        const bannersCollection: IBanners[] = [
          {
            ...banners,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addBannersToCollectionIfMissing(bannersCollection, banners);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Banners to an array that doesn't contain it", () => {
        const banners: IBanners = sampleWithRequiredData;
        const bannersCollection: IBanners[] = [sampleWithPartialData];
        expectedResult = service.addBannersToCollectionIfMissing(bannersCollection, banners);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(banners);
      });

      it('should add only unique Banners to an array', () => {
        const bannersArray: IBanners[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const bannersCollection: IBanners[] = [sampleWithRequiredData];
        expectedResult = service.addBannersToCollectionIfMissing(bannersCollection, ...bannersArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const banners: IBanners = sampleWithRequiredData;
        const banners2: IBanners = sampleWithPartialData;
        expectedResult = service.addBannersToCollectionIfMissing([], banners, banners2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(banners);
        expect(expectedResult).toContain(banners2);
      });

      it('should accept null and undefined values', () => {
        const banners: IBanners = sampleWithRequiredData;
        expectedResult = service.addBannersToCollectionIfMissing([], null, banners, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(banners);
      });

      it('should return initial array if no Banners is added', () => {
        const bannersCollection: IBanners[] = [sampleWithRequiredData];
        expectedResult = service.addBannersToCollectionIfMissing(bannersCollection, undefined, null);
        expect(expectedResult).toEqual(bannersCollection);
      });
    });

    describe('compareBanners', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareBanners(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareBanners(entity1, entity2);
        const compareResult2 = service.compareBanners(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareBanners(entity1, entity2);
        const compareResult2 = service.compareBanners(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareBanners(entity1, entity2);
        const compareResult2 = service.compareBanners(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});

#pragma version(1)
#pragma rs java_package_name(cho8.photowarp)
#pragma rs_fp_relaxed

#include "rs_core.rsh"
#define C_PI 3.141592653589793238462643383279502884197169399375

const uchar4* input;
rs_allocation output;
int width;
int height;

static uchar4 getPixel(int x, int y) {
	if (y >= height) {
		y = height-1;
	}
	if (y < 0) {
		y = 0;
	}
	if (x >= width) {
		x = width-1;
	}
	if (x < 0) {
		x = 0;
	}
	return input[y*width + x];
}


uchar4 RS_KERNEL bulge(uchar4 in, uint32_t x, uint32_t y) {
	float r, a, rn, cX, cY;

	float xdist, ydist;
	float srcX, srcY;

	cX = (float) width / 2.0f;
	cY = (float) height / 2.0f;

	xdist = ((float) x / (float) width);
	ydist = ((float) y / (float) height);

	r = sqrt(pow((xdist-0.5f), 2) + pow((ydist-0.5f), 2));
	a = atan2((xdist-0.5f), (ydist-0.5f));
	rn = pow(r, 2.0f)/0.5f;

	srcX = (rn * sin(a) + 0.5f) * (float)width;
	srcY = (rn * cos(a) + 0.5f) * (float)height;

	return getPixel((int)srcX, (int)srcY);

}



uchar4 RS_KERNEL swirl(uchar4 in, uint32_t x, uint32_t y) {
	int srcX, srcY;
    	float relX, relY, cX, cY;
    	float angle, new_angle, radius;

    	cX = (float) width / 2.0f;
    	cY = (float) height / 2.0f;
    	relY = cY-y;

    	relX = x - cX;
    	if (relX != 0)
    	{
    		angle = atan( fabs(relY) / fabs(relX));
    		if (relX > 0 && relY < 0) angle = 2.0f * C_PI - angle;
    		else if (relX <= 0 && relY >= 0) angle = C_PI - angle;
    		else if (relX <=0 && relY < 0) angle += C_PI;
    	}
    	else
    	{
    		if (relY >= 0) angle = 0.5f * C_PI;
    		else angle = 1.5f * C_PI;
    	}

        radius = sqrt( relX*relX + relY*relY);
    	new_angle = angle + (.001f * radius);

    	srcX = (int)(radius * cos(new_angle)+0.5f);
    	srcY = (int)(radius * sin(new_angle)+0.5f);
    	srcX += cX;
    	srcY += cY;
    	srcY = height - srcY;

    	return getPixel(srcX, srcY);

}

